package com.paypay.framework.exchange.currency.repository.currency.usecase

import com.paypay.framework.exchange.currency.model.CurrencyData
import kotlinx.coroutines.flow.Flow

interface ICurrencyFetchUsecase {
    suspend fun fetchMasterCurrencyConversions(): Flow<Map<String, CurrencyData>>
}