package com.paypay.framework.exchange.currency.persistence

import com.paypay.framework.exchange.currency.model.CurrencyData

interface IExchangeLocalSource {
    suspend fun saveCurrencyExchangeRates(currencyDataList: List<CurrencyData>)
    suspend fun getAllCurrenciesWithExchange(): List<CurrencyData>
}