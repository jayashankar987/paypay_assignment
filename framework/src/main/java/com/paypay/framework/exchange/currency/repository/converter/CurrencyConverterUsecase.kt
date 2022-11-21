package com.paypay.framework.exchange.currency.repository.converter

import com.paypay.data.utils.ResultData.Error
import com.paypay.data.utils.ResultData.Success
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.repository.currency.ICurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyConverterUsecase @Inject constructor(private val currencyRepository: ICurrencyRepository) :
    ICurrencyConverterUsecase {

    override suspend fun getConversionForAllCurrencies(base: String, forceRefresh: Boolean): List<CurrencyData> =
        withContext(Dispatchers.Default) {
            val result = currencyRepository.fetchCurrencyExchangeRates(forceRefresh)
            if (result is Success) {
                val currencyDataList = mutableListOf<CurrencyData>()
                val collection = result.value.filter { it.currencyCode == base }
                val baseValue = if (collection.isNotEmpty()) {
                    collection[0].currencyValue ?: 1.0
                } else 1.0

                result.value.forEach { data ->
                    currencyDataList.add(
                        CurrencyData(
                            currencyName = data.currencyName,
                            currencyValue = ((data.currencyValue ?: -1.0) / baseValue),
                            currencyCode = data.currencyCode
                        )
                    )
                }
                currencyDataList.sortBy { it.currencyCode }
                return@withContext currencyDataList
            } else {
                throw (result as Error).e.ex
            }
        }
}