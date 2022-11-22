package com.paypay.data.datasource.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

abstract class DataRetrofitClient {

    abstract fun getDefaultRetrofit(baseUrl: String): Retrofit

    protected fun getDefaultOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(30, SECONDS)
            .connectTimeout(30, SECONDS)
            .build()
}