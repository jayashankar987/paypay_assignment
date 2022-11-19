package com.paypay.data.datasource.network.parser

interface IMoshiParser {
    fun parseCurrencyRates(data: String?): Map<String, Double>
    fun parseCurrencies(data: String?): Map<String, String>
}