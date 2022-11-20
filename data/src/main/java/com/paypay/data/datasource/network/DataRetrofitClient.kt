package com.paypay.data.datasource.network

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

abstract class DataRetrofitClient {

    private var cacheSize = 2 * 1024 * 1024L
    private var maxAge = 60 * 30  // if online return the same response if is with in 30 minutes
    private var maxStale = 60 * 60 * 24 * 10 //cache for 10 days in case of offline

    constructor()

    constructor(cacheSize: Long, maxAge: Int, maxStale: Int) {
        this.cacheSize = cacheSize
        this.maxAge = maxAge
        this.maxStale = maxStale
    }

    constructor(cacheSize: Long) {
        this.cacheSize = cacheSize
    }

    constructor(maxAge: Int, maxStale: Int) {
        this.maxAge = maxAge
        this.maxStale = maxStale
    }

    private val responseInterceptor by lazy {
        Interceptor { chain: Chain ->
            val response = chain.proceed(chain.request())
            return@Interceptor response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
    }

    private val requestInterceptor by lazy {
        Interceptor { chain: Chain ->
            var request = chain.request()
            if (!hasNetwork()) {
                request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("Pragma").build()
            }
            return@Interceptor chain.proceed(request)
        }

    }

    abstract fun hasNetwork(): Boolean

    /**
     * @getDefaultRetrofit is used to return the retrofit object needed to make network connections from client to server
     * in the below scenario the object might be created multiple times when ever this function is called
     * but this would mean than there is no scalability of this function to change the max-stale and max-Age in the clients
     */
    fun getDefaultRetrofit(baseUrl: String, file: File): Retrofit {
        val cache = Cache(file, cacheSize)
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(MoshiConverterFactory.create())
            .client(getDefaultOkHttpClient(cache)).build()
    }

    private fun getDefaultOkHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addNetworkInterceptor(responseInterceptor)
            .cache(cache).build()
}