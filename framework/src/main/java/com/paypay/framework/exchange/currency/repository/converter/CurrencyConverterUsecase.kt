package com.paypay.framework.exchange.currency.repository.converter

import com.paypay.data.utils.ResultData
import com.paypay.data.utils.ResultData.NoUpdateRequired
import com.paypay.data.utils.ResultData.Success
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.repository.currency.ICurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.TreeMap
import javax.inject.Inject

class CurrencyConverterUsecase @Inject constructor(private val currencyRepository: ICurrencyRepository) :
    ICurrencyConverterUsecase {

    private val masterCurrency = TreeMap<String, CurrencyData>()

    override suspend fun getConversionForAllCurrencies(base: String): List<CurrencyData> = withContext(Dispatchers.Default) {
        when (val result = currencyRepository.fetchCurrencyExchangeRates()) {

            is Success -> {
                masterCurrency.clear()
                masterCurrency.putAll(result.value)
                return@withContext generateCurrencyDataForBase(base = base)
            }
            is NoUpdateRequired -> {
                return@withContext emptyList<CurrencyData>()
            }
            else -> {
                val error = result as ResultData.Error
                throw Exception(error.ex.message)
            }
        }
    }

    override suspend fun fetchCachedCurrencyDetails(base: String): List<CurrencyData> {
        return if(masterCurrency.isEmpty()) {
            getConversionForAllCurrencies(base)
        } else {
            generateCurrencyDataForBase(base)
        }
    }

    private fun generateCurrencyDataForBase(base: String): List<CurrencyData> {
        val currencyDataList = mutableListOf<CurrencyData>()
        val baseCurrencyValue = masterCurrency[base]?.currencyValue ?: -1.0
        masterCurrency.entries.forEach { entry ->
            val currencyValue = entry.value.currencyValue ?: -1.0
            currencyDataList.add(
                CurrencyData(
                    currencyCode = entry.key, currencyName = entry.value.currencyName, currencyValue = (currencyValue / baseCurrencyValue)
                )
            )
        }
        return currencyDataList
    }
}