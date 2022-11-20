package com.paypay.framework.exchange.currency.repository.currency

import android.util.ArrayMap
import android.util.Log
import com.paypay.data.datasource.network.IExchangeNetworkSource
import com.paypay.data.utils.ResultData
import com.paypay.data.utils.ResultData.Error
import com.paypay.data.utils.ResultData.NoUpdateRequired
import com.paypay.data.utils.ResultData.Success
import com.paypay.framework.BuildConfig
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.IExchangeLocalSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val exchangeNetworkSource: IExchangeNetworkSource, private val exchangeLocalSource: IExchangeLocalSource
) : ICurrencyRepository {

    override suspend fun fetchCurrencyExchangeRates(): ResultData<Map<String, CurrencyData>, out Exception> =
        withContext(Dispatchers.IO) {
            try {
                val rates = exchangeNetworkSource.getCurrenciesExchangeRates(appId = BuildConfig.app_id, base = null)
                val currencies = exchangeNetworkSource.getCurrencies()
                if (currencies is Success<Map<String, String>> && rates is Success<Map<String, Double>>) {
                    val mapData = extractMasterExchange(
                        currencies, rates
                    )
                    exchangeLocalSource.saveCurrencyExchangeRates(mapData.values.toList())
                    return@withContext Success(mapData)
                } else if (currencies is Error || rates is Error) {
                    return@withContext Error(Exception("Error in fetching currencies and currency rates"))
                }
            } catch (e: Exception) {
                return@withContext Error(Exception("Unable to load data"))
            }
            return@withContext NoUpdateRequired
        }

    override suspend fun fetchCurrentExchangeRatesFromLocal(): ResultData<HashMap<String, CurrencyData>, out Exception> =
        withContext(Dispatchers.IO) {
            try {
                val result = exchangeLocalSource.getAllCurrenciesWithExchange()
                val mapData = HashMap<String, CurrencyData>()
                for (currencyData in result) {
                    mapData[currencyData.currencyCode] = currencyData
                }
                return@withContext Success(mapData)
            } catch (e: Exception) {
                return@withContext Error(Exception("Error fetching local data"))
            }
        }

    private fun extractMasterExchange(
        currencies: Success<Map<String, String>>, rates: Success<Map<String, Double>>
    ): Map<String, CurrencyData> {
        val currencyMap = ArrayMap<String, CurrencyData>()
        currencies.value.entries.forEach { entry ->
            val currencyData = CurrencyData(
                currencyCode = entry.key, currencyName = entry.value, currencyValue = rates.value[entry.key]
            )
            currencyMap[entry.key] = currencyData
        }
        return currencyMap
    }
}