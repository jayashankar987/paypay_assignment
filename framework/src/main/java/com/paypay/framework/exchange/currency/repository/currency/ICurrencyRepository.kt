package com.paypay.framework.exchange.currency.repository.currency

import com.paypay.data.utils.DataFetchError
import com.paypay.data.utils.ResultData
import com.paypay.framework.exchange.currency.model.CurrencyData

interface ICurrencyRepository {
    suspend fun fetchCurrencyExchangeRates(forceRefresh: Boolean? = false): ResultData<List<CurrencyData>, DataFetchError>
}