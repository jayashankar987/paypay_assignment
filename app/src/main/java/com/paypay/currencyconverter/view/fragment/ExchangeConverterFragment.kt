package com.paypay.currencyconverter.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paypay.currencyconverter.databinding.FragmentFirstBinding
import com.paypay.currencyconverter.videomodel.CurrenciesFetchState
import com.paypay.currencyconverter.videomodel.ExchangeCurrencyViewModel
import com.paypay.currencyconverter.view.adapter.CurrencyListAdapter
import com.paypay.currencyconverter.view.adapter.ExchangeConverterListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ExchangeConverterFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val viewModel: ExchangeCurrencyViewModel by viewModels()

    private lateinit var exchangeAdapter: ExchangeConverterListAdapter
    private lateinit var currenciesAdapter: CurrencyListAdapter
    private var selectedCurrency = "USD"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.fetchCurrencyRates("USD", 1.0)

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is CurrenciesFetchState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorLayout.visibility = View.GONE

                        exchangeAdapter.addItems(state.data)
                        Log.d("jaya", "jaya ${state.data}")
                        binding.recyclerView.visibility = View.VISIBLE
                        Toast.makeText(context, "Success : ${state.data.size}", Toast.LENGTH_SHORT).show()
                    }
                    is CurrenciesFetchState.Error -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.errorLayout.visibility = View.VISIBLE
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                    is CurrenciesFetchState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is CurrenciesFetchState.Init -> {
                        Toast.makeText(context, "Init", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initView() {
        binding.recyclerView.apply {
            exchangeAdapter = ExchangeConverterListAdapter()
            adapter = exchangeAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        binding.currencySpinner.apply {
            currenciesAdapter = CurrencyListAdapter()
            //adapter = currenciesAdapter
        }

        binding.inputText.addTextChangedListener {
            it?.let {
                if (it.isNotEmpty()) {
                    viewModel.fetchCurrencyRates(base = selectedCurrency, input = it.toString().toDouble())
                }
            }
        }

        binding.retry.setOnClickListener {
            if (binding.inputText.text?.isNotEmpty() == true) {
                viewModel.fetchCurrencyRates(base = selectedCurrency, input = binding.inputText.text.toString().toDouble())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}