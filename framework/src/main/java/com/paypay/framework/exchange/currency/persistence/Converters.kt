package com.paypay.framework.exchange.currency.persistence

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class Converters constructor(moshi: Moshi) {

    private val membersType = Types.newParameterizedType(List::class.java, CurrencyData::class.java)
    private val membersAdapter = moshi.adapter<List<CurrencyData>>(membersType)

    @TypeConverter
    fun fromStringToCurrencyExchangeList(data: String): List<CurrencyData> {
        return membersAdapter.fromJson(data).orEmpty()
    }

    @TypeConverter
    fun fromCurrencyExchangeListToString(list: List<CurrencyData>): String {
        return membersAdapter.toJson(list)
    }
}