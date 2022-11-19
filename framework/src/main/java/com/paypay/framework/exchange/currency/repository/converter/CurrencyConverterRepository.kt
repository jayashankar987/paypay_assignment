package com.paypay.framework.exchange.currency.repository.converter

import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.repository.currency.usecase.ICurrencyFetchUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyConverterRepository @Inject constructor(private val currencyFetchUsecase: ICurrencyFetchUsecase) :
    ICurrencyConverterRepository {

    private val masterCurrency = HashMap<String, CurrencyData>()

    override suspend fun getConversionForAllCurrencies(base: String, input: Float): Flow<List<CurrencyData>> {
        return currencyFetchUsecase.fetchMasterCurrencyConversions().map {
            if (it.isNotEmpty()) {
                masterCurrency.clear()
                masterCurrency.putAll(it)
                return@map generateCurrencyDataForBase(base = base, input = input)
            } else {
                return@map emptyList()
            }
        }.flowOn(Dispatchers.Default)
    }

    private fun generateCurrencyDataForBase(base: String, input: Float): List<CurrencyData> {
        val currencyDataList = mutableListOf<CurrencyData>()
        val baseCurrencyValue = masterCurrency[base]?.value ?: -1.0
        masterCurrency.entries.forEach { entry ->
            val currencyValue = entry.value.value ?: -1.0
            currencyDataList.add(
                CurrencyData(
                    currencyCode = entry.key,
                    currencyName = entry.value.currencyName,
                    value = input * (currencyValue / baseCurrencyValue)
                )
            )
        }
        return currencyDataList
    }
}