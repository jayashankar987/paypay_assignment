package com.paypay.framework.exchange.currency.repository.currency

import android.util.ArrayMap
import com.paypay.data.datasource.network.IExchangeNetworkSource
import com.paypay.data.datasource.network.NetworkResponse.Error
import com.paypay.data.datasource.network.NetworkResponse.NoUpdateRequired
import com.paypay.data.datasource.network.NetworkResponse.Success
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.IExchangeLocalSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val exchangeNetworkSource: IExchangeNetworkSource, private val exchangeLocalSource: IExchangeLocalSource
) : ICurrencyRepository {

    override suspend fun fetchCurrencyExchangeRates(): Flow<Map<String, CurrencyData>> {
        return exchangeNetworkSource.getCurrenciesExchangeRates(appId = "", base = null)
            .zip(exchangeNetworkSource.getCurrencies()) { rates, currencies ->
                if (currencies is Success && rates is Success) {
                    val mapData =
                        extractMasterExchange(currencies as Success<Map<String, String>>,
                            rates as Success<Map<String, Double>>)
                    //exchangeLocalSource.saveCurrencyExchangeRates(mapData.values.toList())
                    return@zip Success(mapData)
                } else if (rates is NoUpdateRequired && currencies is NoUpdateRequired) {
                    return@zip NoUpdateRequired
                } else {
                    return@zip Error("unable to fetch network data")
                }
            }.flatMapConcat { currencyData ->
                if(currencyData is Success<*>) {
                   return@flatMapConcat flow {  emit(currencyData.value as Map<String, CurrencyData>) }
                } else if (currencyData is NoUpdateRequired) {
                    return@flatMapConcat flow { emit(emptyMap()) }
                }
                return@flatMapConcat fetchCurrentExchangeRatesFromLocal()
            }.flowOn(Dispatchers.IO)

    }

    override suspend fun fetchCurrentExchangeRatesFromLocal(): Flow<Map<String,CurrencyData>> {
        return exchangeLocalSource.getAllCurrenciesWithExchange().map {
            val mapData = HashMap<String, CurrencyData>()
            for(currencyData in it) {
                mapData[currencyData.currencyCode] = currencyData
            }
            return@map mapData
        }
    }

    private fun extractMasterExchange(
        currencies: Success<Map<String, String>>, rates: Success<Map<String, Double>>
    ): Map<String, CurrencyData> {
        val currencyMap = ArrayMap<String, CurrencyData>()
        currencies.value.entries.forEach { entry ->
            val currencyData = CurrencyData(
                currencyCode = entry.key, currencyName = entry.value, value = rates.value.getValue(entry.key)
            )
            currencyMap[entry.key] = currencyData
        }
        return currencyMap
    }
}