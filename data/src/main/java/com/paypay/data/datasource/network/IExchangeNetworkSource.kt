package com.paypay.data.datasource.network

import com.paypay.data.model.CurrencyResponse

interface IExchangeNetworkSource {
    suspend fun getCurrenciesExchangeRates(appId: String, base: String? = "USD"): CurrencyResponse
    suspend fun getCurrencies(): Map<String, String>
}