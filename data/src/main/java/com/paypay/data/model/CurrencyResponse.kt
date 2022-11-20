package com.paypay.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyResponse(
    @field:Json(name = "base") val base: String,
    @field:Json(name = "disclaimer") val disclaimer: String,
    @field:Json(name = "license") val license: String,
    @field:Json(name = "rates") val rates: Map<String, Double>,
    @field:Json(name = "timestamp") val timestamp: Int
)

