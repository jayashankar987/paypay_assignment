package com.paypay.framework.exchange.currency.repository.currency

import com.paypay.data.utils.ResultData
import com.paypay.framework.exchange.currency.model.CurrencyData

interface ICurrencyRepository {
    suspend fun fetchCurrencyExchangeRates(): ResultData<Map<String, CurrencyData>, out Exception>
    suspend fun fetchCurrentExchangeRatesFromLocal(): ResultData<HashMap<String, CurrencyData>, out Exception>
}