package com.paypay.framework.exchange.currency.repository.converter

import com.paypay.framework.exchange.currency.model.CurrencyData

interface ICurrencyConverterUsecase {
    suspend fun getConversionForAllCurrencies(base: String, forceRefresh: Boolean): List<CurrencyData>
}