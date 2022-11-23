package com.paypay.framework.exchange.currency.repository.converter

import android.util.Log
import com.paypay.data.utils.onError
import com.paypay.data.utils.onSuccess
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.repository.currency.ICurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyConverterUsecase @Inject constructor(private val currencyRepository: ICurrencyRepository) :
    ICurrencyConverterUsecase {

    override suspend fun getConversionForAllCurrencies(
        base: String, forceRefresh: Boolean
    ): List<CurrencyData> = withContext(Dispatchers.Default) {
        val currencyDataList = mutableListOf<CurrencyData>()
        currencyRepository.fetchCurrencyExchangeRates(forceRefresh).onSuccess { result ->
            val collection = result.filter { it.currencyCode == base }
            val baseValue = if (collection.isNotEmpty()) {
                collection[0].currencyValue ?: 1.0
            } else 1.0


            result.forEach { data ->
                currencyDataList.add(
                    CurrencyData(
                        currencyName = data.currencyName,
                        currencyValue = ((data.currencyValue ?: -1.0) / baseValue),
                        currencyCode = data.currencyCode
                    )

                )
            }
            currencyDataList.sortBy { it.currencyCode }
        }.onError {
            throw it.ex
        }
        return@withContext currencyDataList
    }
}