package com.paypay.framework.exchange.currency.repository.converter

import com.paypay.framework.exchange.currency.model.CurrencyData
import kotlinx.coroutines.flow.Flow

interface ICurrencyConverterRepository {

    suspend fun getConversionForAllCurrencies(base: String, input: Float): Flow<List<CurrencyData>>
}