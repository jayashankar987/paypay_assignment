package com.paypay.currencyconverter.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil.Callback
import com.paypay.currencyconverter.R
import com.paypay.currencyconverter.base.BaseRecyclerViewAdapter
import com.paypay.currencyconverter.base.BaseViewHolder
import com.paypay.currencyconverter.util.Utils
import com.paypay.framework.exchange.currency.model.CurrencyData

class ExchangeConverterListAdapter : BaseRecyclerViewAdapter<CurrencyData>() {


    override val callback: (input: List<CurrencyData>) -> Callback
        get() = { inputData: List<CurrencyData> ->
            object : Callback() {
                override fun getOldListSize(): Int = getData().size

                override fun getNewListSize(): Int = inputData.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return getData()[oldItemPosition] == inputData[newItemPosition]
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return getData()[oldItemPosition] == inputData[newItemPosition]
                }
            }

        }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.currency_item_view, parent, false)
        return CurrencyExchangeViewHolder(view)
    }

    class CurrencyExchangeViewHolder(view: View) : BaseViewHolder(view) {
        private val currencyCode: AppCompatTextView = view.findViewById(R.id.exchangeCode)
        private val currencyName: AppCompatTextView = view.findViewById(R.id.exchangeName)
        private val value: AppCompatTextView = view.findViewById(R.id.value)

        override fun onBind(data: Any?) {
            val currencyData = data as? CurrencyData ?: return
            currencyCode.text = currencyData.currencyCode
            currencyName.text = currencyData.currencyName
            val dataValue = if (currencyData.currencyValue == null) "NaN"
            else Utils.getDecimalDataForDouble(currencyData.currencyValue!!)
            value.text = dataValue
        }

        override fun destroy() {

        }
    }
}