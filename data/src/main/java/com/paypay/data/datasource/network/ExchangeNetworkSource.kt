package com.paypay.data.datasource.network

import com.paypay.data.datasource.network.parser.IMoshiParser
import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.data.utils.Utils.isUpdateRequired
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExchangeNetworkSource constructor(
    private val exchangeService: ExchangeService, private val moshiParser: IMoshiParser
) : IExchangeNetworkSource {

    override suspend fun getCurrenciesExchangeRates(appId: String, base: String?): Flow<NetworkResponse<Any>> {
        return flow {
            val response = exchangeService.getLatestRates(appId = appId, base = base)
            if (!isUpdateRequired(response)) {
                emit(NetworkResponse.NoUpdateRequired)
            } else if (response.isSuccessful && response.body()?.rates?.toString() != null) {
                val map = moshiParser.parseCurrencyRates(response.body()?.rates?.toString())
                emit(NetworkResponse.Success(map))
            } else {
                //could have customised the Error but let me see if I can at last
                emit(NetworkResponse.Error(Throwable("Error in fetching data")))
            }
        }
    }

    override suspend fun getCurrencies(): Flow<NetworkResponse<Any>> {
        return flow {
            val response = exchangeService.getCurrencies()
            val body = response.body().toString()
            if (!isUpdateRequired(response)) {
                emit(NetworkResponse.NoUpdateRequired)
            } else if (response.isSuccessful) {
                val map = moshiParser.parseCurrencyRates(body)
                emit(NetworkResponse.Success(map))
            } else {
                //could have customised the Error but let me see if I can at last
                emit(NetworkResponse.Error(Throwable("Error in fetching data")))
            }
        }
    }
}