package com.paypay.framework.exchange.currency.persistence

import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.dao.IExchangeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExchangeLocalSource @Inject constructor(private val exchangeDao: IExchangeDao) : IExchangeLocalSource {

    override suspend fun saveCurrencyExchangeRates(currencyDataList: List<CurrencyData>): Flow<Unit> {
        return flow {
            exchangeDao.insertCurrencyExchangeRates(*currencyDataList.toTypedArray())
            emit(Unit)
        }

    }

    override suspend fun getAllCurrenciesWithExchange(): Flow<List<CurrencyData>> {
        return flow {

            emit(exchangeDao.getAllCurrencyExchangeRates())
        }
    }
}