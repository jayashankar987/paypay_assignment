package com.paypay.data.datasource.network.parser

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class ExchangeResponseParser : IMoshiParser {

    private val moshi: Moshi by lazy { Moshi.Builder().build() }

    override fun parseCurrencyRates(data: String?): Map<String, Double> {
        val type = Types.newParameterizedType(
            MutableMap::class.java, String::class.java, Double::class.java
        )
        val adapter: JsonAdapter<Map<String, Double>> = moshi.adapter(type)
        return data?.let { adapter.fromJson(it) } ?: emptyMap()
    }

    override fun parseCurrencies(data: String?): Map<String, String> {
        val type = Types.newParameterizedType(
            MutableMap::class.java, String::class.java, String::class.java
        )
        val adapter: JsonAdapter<Map<String, String>> = moshi.adapter(type)
        return data?.let { adapter.fromJson(it) } ?: emptyMap()
    }
}