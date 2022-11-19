package com.paypay.framework.exchange.currency.persistence

import com.paypay.framework.exchange.currency.model.CurrencyData
import kotlinx.coroutines.flow.Flow

interface IExchangeLocalSource {
    suspend fun saveCurrencyExchangeRates(currencyDataList: List<CurrencyData>): Flow<Unit>
    suspend fun getAllCurrenciesWithExchange(): Flow<List<CurrencyData>>
}