package com.paypay.currencyconverter.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paypay.currencyconverter.R
import com.paypay.currencyconverter.databinding.FragmentFirstBinding
import com.paypay.currencyconverter.videomodel.CurrenciesFetchState
import com.paypay.currencyconverter.videomodel.ExchangeCurrencyViewModel
import com.paypay.currencyconverter.view.adapter.CurrencyListAdapter
import com.paypay.currencyconverter.view.adapter.ExchangeConverterListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ExchangeConverterFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var _binding: FragmentFirstBinding? = null
    private val viewModel: ExchangeCurrencyViewModel by viewModels()

    private lateinit var exchangeAdapter: ExchangeConverterListAdapter
    private lateinit var currenciesAdapter: CurrencyListAdapter
    private var selectedCurrency = "USD"
    private var filterPopup: PopupWindow? = null
    private var flowJob: Job? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val clickListener: (currencyCode: String, pos: Int) -> Unit = { code, _ ->
        dismissPopup()
        selectedCurrency = code
        binding.currencySpinner.text = code
        if (binding.inputText.text?.isNotEmpty() == true) {
            viewModel.fetchCurrencyRates(
                base = selectedCurrency, input = binding.inputText.text.toString().toDouble()
            )
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        private var previousInput: String = ""
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val input = p0?.toString()?.trim() ?: ""
            if (previousInput == input) {
                return
            }
            previousInput = input
            launch {
                delay(300)
                if (input != previousInput) {
                    return@launch
                }
                val inputValue = if (input.isEmpty()) 1.0 else input.toDouble()
                viewModel.fetchCurrencyRates(selectedCurrency, inputValue)
            }
        }

        override fun afterTextChanged(p0: Editable?) = Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        flowJob?.cancel()
        flowJob = lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is CurrenciesFetchState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorLayout.visibility = View.GONE
                        if (state.data.isNotEmpty()) {
                            exchangeAdapter.addItems(data = state.data)
                        }
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    is CurrenciesFetchState.Error -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.errorLayout.visibility = View.VISIBLE
                    }
                    is CurrenciesFetchState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is CurrenciesFetchState.Init -> {
                        state.data?.let { currencies ->
                            currenciesAdapter.addItems(data = currencies)
                        }
                    }
                }
            }
        }
    }

    /**
     * init all the UI elements with in the fragment
     * 1. The Input Text
     * 2. PopupWindow to select the currency
     * 3. Recyclerview for current data
     * 4. Error view
     */
    private fun initView() {
        binding.recyclerView.apply {
            itemAnimator = null
            exchangeAdapter = ExchangeConverterListAdapter()
            adapter = exchangeAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        binding.currencySpinner.text = selectedCurrency

        currenciesAdapter = CurrencyListAdapter(clickListener)

        binding.currencySpinner.setOnClickListener {
            dismissPopup()
            filterPopup = showPopupWindow()
            filterPopup?.isOutsideTouchable = true
            filterPopup?.isFocusable = true
            filterPopup?.showAsDropDown(binding.currencySpinner)
        }

        binding.inputText.addTextChangedListener(textWatcher)

        binding.retry.setOnClickListener {
            if (binding.inputText.text?.isNotEmpty() == true) {
                viewModel.fetchCurrencyRates(base = selectedCurrency, input = binding.inputText.text.toString().toDouble())
            }
        }

    }

    private fun dismissPopup() {
        filterPopup?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        filterPopup = null
    }

    private fun showPopupWindow(): PopupWindow {
        val popupView = LayoutInflater.from(activity).inflate(R.layout.currency_selector_layout, null)
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.apply {
            adapter = currenciesAdapter
            addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        }

        return PopupWindow(popupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onStop() {
        flowJob?.cancel()
        filterPopup = null
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        flowJob = null
        _binding = null
    }
}