package com.paypay.framework.exchange.currency.repository.currency

import android.content.SharedPreferences
import com.paypay.data.datasource.network.IExchangeNetworkSource
import com.paypay.data.exception.AppException
import com.paypay.data.exception.AppException.Offline
import com.paypay.data.utils.DataFetchError
import com.paypay.data.utils.ResultData
import com.paypay.data.utils.onError
import com.paypay.data.utils.onSuccess
import com.paypay.data.utils.runSuspendCatching
import com.paypay.framework.BuildConfig
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.IExchangeLocalSource
import com.paypay.framework.exchange.currency.utils.FrameworkConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val exchangeNetworkSource: IExchangeNetworkSource,
    private val exchangeLocalSource: IExchangeLocalSource,
    private val sharedPreferences: SharedPreferences
) : ICurrencyRepository {

    override suspend fun fetchCurrencyExchangeRates(forceRefresh: Boolean?): ResultData<List<CurrencyData>, DataFetchError> =
        runSuspendCatching {
            var currencyDataList = exchangeLocalSource.getAllCurrenciesWithExchange()
            if (currencyDataList.isEmpty() || true == forceRefresh) {
                val rates = exchangeNetworkSource.getCurrenciesExchangeRates(appId = BuildConfig.app_id, base = null).rates
                val currencies = exchangeNetworkSource.getCurrencies()

                currencyDataList = extractMasterExchange(currencies, rates)

                CoroutineScope(Dispatchers.IO).launch {
                    exchangeLocalSource.saveCurrencyExchangeRates(currencyDataList)
                    sharedPreferences.edit().putLong(
                        FrameworkConstants.PREF_LAST_SYNC_TIMESTAMP, System.currentTimeMillis()
                    ).apply()
                }

            }
            return@runSuspendCatching currencyDataList
        }.onError {
            if (it.ex is Offline) {
                return fetchCurrencyExchangeRatesFromLocal()
            }
        }

    private suspend fun fetchCurrencyExchangeRatesFromLocal(): ResultData<List<CurrencyData>, DataFetchError> =
        runSuspendCatching {
            exchangeLocalSource.getAllCurrenciesWithExchange()
        }.onSuccess {
            if (it.isEmpty()) {
                return ResultData.Error(DataFetchError(AppException.mapper(Throwable("No data to display"))))
            }
        }

    private fun extractMasterExchange(
        currencies: Map<String, String>, rates: Map<String, Double>?
    ): List<CurrencyData> {
        val currencyDataList = mutableListOf<CurrencyData>()
        currencies.entries.forEach { entry ->
            val currencyData = CurrencyData(
                currencyCode = entry.key, currencyName = entry.value, currencyValue = rates?.get(entry.key) ?: 0.0
            )
            currencyDataList.add(currencyData)
        }
        return currencyDataList
    }
}