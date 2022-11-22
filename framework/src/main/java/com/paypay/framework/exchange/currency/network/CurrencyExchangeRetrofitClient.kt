package com.paypay.framework.exchange.currency.network

import com.paypay.data.datasource.network.DataRetrofitClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CurrencyExchangeRetrofitClient : DataRetrofitClient() {
    override fun getDefaultRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(MoshiConverterFactory.create())
            .client(getDefaultOkHttpClient()).build()
    }
}