package com.paypay.data.datasource.network

import com.paypay.data.utils.ResultData

interface IExchangeNetworkSource {
    suspend fun getCurrenciesExchangeRates(appId: String, base: String? = "USD"): ResultData<Map<String, Double>, out Exception>
    suspend fun getCurrencies(): ResultData<Map<String, String>, out Exception>
}