package com.paypay.framework.exchange.currency.utils

object FrameworkConstants {
    const val HTTP_CACHE = "http"

    const val PREF_LAST_SYNC_TIMESTAMP = "last_synced"

    object TableName {
        const val CURRENCY_EXCHANGE_ENTITY = "current_exchange"
    }

    object CurrencyDetails {
        object Column {
            const val CURRENCY_NAME = "currency_name"
            const val CURRENCY_CODE = "currency_code"
            const val CURRENCY_VALUE = "currency_value"
        }
    }
}