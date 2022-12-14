package com.paypay.framework.exchange.currency.di

import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.framework.BuildConfig
import com.paypay.framework.exchange.currency.network.CurrencyExchangeRetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    /**
     * trying to inject a singleton retrofit object to be used for all connections
     */
    @Provides
    fun providesRetrofit(): Retrofit {
        return CurrencyExchangeRetrofitClient().getDefaultRetrofit(
            baseUrl = BuildConfig.base_url
        )
    }

    @Provides
    fun providesExchangeApiService(retrofit: Retrofit): ExchangeService {
        return retrofit.create(ExchangeService::class.java)
    }

}