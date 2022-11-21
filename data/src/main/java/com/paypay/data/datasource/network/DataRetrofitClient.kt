package com.paypay.data.datasource.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

abstract class DataRetrofitClient {

    abstract fun hasNetwork(): Boolean

    /**
     * @getDefaultRetrofit is used to return the retrofit object needed to make network connections from client to server
     * in the below scenario the object might be created multiple times when ever this function is called
     * but this would mean than there is no scalability of this function to change the max-stale and max-Age in the clients
     */
    fun getDefaultRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(MoshiConverterFactory.create())
            .client(getDefaultOkHttpClient()).build()
    }

    private fun getDefaultOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().callTimeout(30, SECONDS).connectTimeout(30, SECONDS).build()
}