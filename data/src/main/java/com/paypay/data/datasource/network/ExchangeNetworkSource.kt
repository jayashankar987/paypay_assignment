package com.paypay.data.datasource.network

import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.data.model.CurrencyResponse

class ExchangeNetworkSource constructor(
    private val exchangeService: ExchangeService
) : IExchangeNetworkSource {

    override suspend fun getCurrenciesExchangeRates(
        appId: String, base: String?
    ): CurrencyResponse = exchangeService.getLatestRates(appId = appId, base = base)

    override suspend fun getCurrencies(): Map<String, String> = exchangeService.getCurrencies()
}