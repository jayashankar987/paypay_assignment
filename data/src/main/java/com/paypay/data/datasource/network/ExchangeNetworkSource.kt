package com.paypay.data.datasource.network

import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.data.utils.ResultData
import com.paypay.data.utils.Utils.isUpdateRequired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExchangeNetworkSource constructor(
    private val exchangeService: ExchangeService
) : IExchangeNetworkSource {

    override suspend fun getCurrenciesExchangeRates(
        appId: String, base: String?
    ): ResultData<Map<String, Double>, out Exception> = withContext(Dispatchers.IO) {
        try {
            val response = exchangeService.getLatestRates(appId = appId, base = base)
            if (!isUpdateRequired(response)) {
                return@withContext (ResultData.NoUpdateRequired)
            } else if (response.isSuccessful && response.body()?.rates != null) {
                val map = response.body()?.rates ?: emptyMap()
                return@withContext (ResultData.Success(map))
            } else {
                //could have customised the Error but let me see if I can at last
                return@withContext (ResultData.Error(Exception("Error in fetching data")))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext ResultData.Error(Exception("Error in fetching data"))
        }
    }

    override suspend fun getCurrencies(): ResultData<Map<String, String>, out Exception> = withContext(Dispatchers.IO) {
        try {
            val response = exchangeService.getCurrencies()
            val body = response.body()
            if (!isUpdateRequired(response)) {
                return@withContext (ResultData.NoUpdateRequired)
            } else if (response.isSuccessful) {
                val map = body ?: emptyMap()
                return@withContext (ResultData.Success(map))
            } else {
                //could have customised the Error but let me see if I can at last
                return@withContext (ResultData.Error(Exception("Error in fetching data")))
            }
        } catch (e: Exception) {
            return@withContext (ResultData.Error(Exception("Error in fetching data")))
        }
    }
}