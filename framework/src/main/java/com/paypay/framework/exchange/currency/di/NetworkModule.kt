package com.paypay.framework.exchange.currency.di

import android.content.Context
import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.framework.BuildConfig
import com.paypay.framework.exchange.currency.network.CurrencyExchangeRetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    /**
     * trying to inject a singleton retrofit object to be used for all connections
     */
    @Provides
    fun providesRetrofit(@ApplicationContext applicationContext: Context): Retrofit {
        return CurrencyExchangeRetrofitClient(context = applicationContext).getDefaultRetrofit(
            baseUrl = BuildConfig.base_url
        )
    }

    @Provides
    fun providesExchangeApiService(retrofit: Retrofit): ExchangeService {
        return retrofit.create(ExchangeService::class.java)
    }

}