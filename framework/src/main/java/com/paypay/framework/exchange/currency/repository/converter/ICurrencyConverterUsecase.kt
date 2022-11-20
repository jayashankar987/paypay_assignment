package com.paypay.framework.exchange.currency.repository.converter

import com.paypay.framework.exchange.currency.model.CurrencyData

interface ICurrencyConverterUsecase {

    suspend fun getConversionForAllCurrencies(base: String): List<CurrencyData>
    suspend fun fetchCachedCurrencyDetails(base: String): List<CurrencyData>
}