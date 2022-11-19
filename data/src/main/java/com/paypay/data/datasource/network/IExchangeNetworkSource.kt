package com.paypay.data.datasource.network

import kotlinx.coroutines.flow.Flow

interface IExchangeNetworkSource {
    suspend fun getCurrenciesExchangeRates(appId: String, base: String?): Flow<NetworkResponse<Any>>
    suspend fun getCurrencies(): Flow<NetworkResponse<Any>>
}