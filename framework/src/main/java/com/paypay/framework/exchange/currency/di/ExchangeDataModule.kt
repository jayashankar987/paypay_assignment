package com.paypay.framework.exchange.currency.di

import com.paypay.data.datasource.network.ExchangeNetworkSource
import com.paypay.data.datasource.network.IExchangeNetworkSource
import com.paypay.data.datasource.network.parser.ExchangeResponseParser
import com.paypay.data.datasource.network.parser.IMoshiParser
import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.framework.exchange.currency.persistence.ExchangeLocalSource
import com.paypay.framework.exchange.currency.persistence.IExchangeLocalSource
import com.paypay.framework.exchange.currency.persistence.dao.IExchangeDao
import com.paypay.framework.exchange.currency.repository.converter.CurrencyConverterRepository
import com.paypay.framework.exchange.currency.repository.converter.ICurrencyConverterRepository
import com.paypay.framework.exchange.currency.repository.currency.CurrencyRepository
import com.paypay.framework.exchange.currency.repository.currency.ICurrencyRepository
import com.paypay.framework.exchange.currency.repository.currency.usecase.CurrencyFetchUsecase
import com.paypay.framework.exchange.currency.repository.currency.usecase.ICurrencyFetchUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ExchangeDataModule {

    @Provides
    fun providesExchangeMoshiParser(): IMoshiParser {
        return ExchangeResponseParser()
    }

    @Provides
    fun providesExchangeNetworkSource(
        exchangeService: ExchangeService, moshiParser: IMoshiParser
    ): IExchangeNetworkSource {
        return ExchangeNetworkSource(
            exchangeService = exchangeService, moshiParser = moshiParser
        )
    }

    @Provides
    fun providesExchangeLocalSource(exchangeDao: IExchangeDao): IExchangeLocalSource {
        return ExchangeLocalSource(exchangeDao = exchangeDao)
    }

    @Provides
    fun providesExchangeRepository(
        exchangeNetworkSource: IExchangeNetworkSource, exchangeLocalSource: IExchangeLocalSource
    ): ICurrencyRepository {
        return CurrencyRepository(
            exchangeNetworkSource = exchangeNetworkSource, exchangeLocalSource = exchangeLocalSource
        )
    }

    @Provides
    fun providesCurrencyFetchUsecase(
        currencyRepository: ICurrencyRepository
    ): ICurrencyFetchUsecase {
        return CurrencyFetchUsecase(currencyRepository = currencyRepository)
    }

    @Provides
    fun providesCurrencyConverterRepository(
        currencyFetchUsecase: ICurrencyFetchUsecase
    ): ICurrencyConverterRepository {
        return CurrencyConverterRepository(currencyFetchUsecase = currencyFetchUsecase)
    }

}