package com.paypay.framework.exchange.currency.repository.currency.usecase

import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.repository.currency.ICurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyFetchUsecase @Inject constructor(private val currencyRepository: ICurrencyRepository) : ICurrencyFetchUsecase {

    override suspend fun fetchMasterCurrencyConversions(): Flow<Map<String, CurrencyData>> {
        return currencyRepository.fetchCurrencyExchangeRates()
    }
}