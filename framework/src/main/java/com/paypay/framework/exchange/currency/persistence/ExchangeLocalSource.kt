package com.paypay.framework.exchange.currency.persistence

import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.dao.IExchangeDao
import javax.inject.Inject

class ExchangeLocalSource @Inject constructor(private val exchangeDao: IExchangeDao) : IExchangeLocalSource {

    override suspend fun saveCurrencyExchangeRates(currencyDataList: List<CurrencyData>) {
        return exchangeDao.insertCurrencyExchangeRates(*currencyDataList.toTypedArray())
    }

    override suspend fun getAllCurrenciesWithExchange(): List<CurrencyData> {
        return exchangeDao.getAllCurrencyExchangeRates()
    }
}