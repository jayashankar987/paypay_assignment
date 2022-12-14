package com.paypay.currencyconverter.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil.Callback
import com.paypay.currencyconverter.R
import com.paypay.currencyconverter.base.BaseRecyclerViewAdapter
import com.paypay.currencyconverter.base.BaseViewHolder

class CurrencyListAdapter(private val clickListener: (item: String, pos: Int) -> Unit) :
    BaseRecyclerViewAdapter<Pair<String, String>>() {

    override val callback: (input: List<Pair<String, String>>) -> Callback
        get() = { inputData: List<Pair<String, String>> ->
            object : Callback() {
                override fun getOldListSize(): Int = getData().size

                override fun getNewListSize(): Int = inputData.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return getData()[oldItemPosition].first == inputData[newItemPosition].first
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val (code, name) = getData()[oldItemPosition]
                    val (codeNew, nameNew) = getData()[newItemPosition]
                    return  code == codeNew && name == nameNew
                }
            }

        }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.spinner_item_adapter, parent, false)
        return CurrencyExchangeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            clickListener.invoke(getData()[position].first, position)
        }
    }

    class CurrencyExchangeViewHolder(view: View) : BaseViewHolder(view) {
        private val currencyCode: AppCompatTextView = view.findViewById(R.id.exchangeCode)
        private val currencyName: AppCompatTextView = view.findViewById(R.id.exchangeName)

        override fun onBind(data: Any?) {
            val currency = data as? Pair<String, String> ?: return
            currencyCode.text = currency.first
            currencyName.text = currency.second
        }

        override fun destroy() {
            //destory any view items if needed
        }
    }
}