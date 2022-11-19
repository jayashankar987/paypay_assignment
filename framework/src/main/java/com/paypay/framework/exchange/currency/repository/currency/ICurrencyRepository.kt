package com.paypay.framework.exchange.currency.repository.currency

import com.paypay.framework.exchange.currency.model.CurrencyData
import kotlinx.coroutines.flow.Flow

interface ICurrencyRepository {
    suspend fun fetchCurrencyExchangeRates(): Flow<Map<String, CurrencyData>>
    suspend fun fetchCurrentExchangeRatesFromLocal(): Flow<Map<String, CurrencyData>>
}