package com.paypay.framework.exchange.currency.di

import android.content.Context
import android.content.SharedPreferences
import com.paypay.data.datasource.network.ExchangeNetworkSource
import com.paypay.data.datasource.network.IExchangeNetworkSource
import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.framework.exchange.currency.persistence.ExchangeLocalSource
import com.paypay.framework.exchange.currency.persistence.IExchangeLocalSource
import com.paypay.framework.exchange.currency.persistence.dao.IExchangeDao
import com.paypay.framework.exchange.currency.repository.converter.CurrencyConverterUsecase
import com.paypay.framework.exchange.currency.repository.converter.ICurrencyConverterUsecase
import com.paypay.framework.exchange.currency.repository.currency.CurrencyRepository
import com.paypay.framework.exchange.currency.repository.currency.ICurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ExchangeDataModule {

    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("repository_pref", Context.MODE_PRIVATE)
    }

    @Provides
    fun providesExchangeNetworkSource(
        exchangeService: ExchangeService
    ): IExchangeNetworkSource {
        return ExchangeNetworkSource(
            exchangeService = exchangeService
        )
    }

    @Provides
    fun providesExchangeLocalSource(exchangeDao: IExchangeDao): IExchangeLocalSource {
        return ExchangeLocalSource(exchangeDao = exchangeDao)
    }

    @Provides
    fun providesExchangeRepository(
        exchangeNetworkSource: IExchangeNetworkSource,
        exchangeLocalSource: IExchangeLocalSource,
        sharedPreferences: SharedPreferences
    ): ICurrencyRepository {
        return CurrencyRepository(
            exchangeNetworkSource = exchangeNetworkSource,
            exchangeLocalSource = exchangeLocalSource,
            sharedPreferences = sharedPreferences
        )
    }

    @Provides
    fun providesCurrencyConverterRepository(
        currencyRepository: ICurrencyRepository
    ): ICurrencyConverterUsecase {
        return CurrencyConverterUsecase(currencyRepository = currencyRepository)
    }

}