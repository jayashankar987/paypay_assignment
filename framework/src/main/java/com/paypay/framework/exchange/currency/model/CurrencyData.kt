package com.paypay.framework.exchange.currency.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paypay.framework.exchange.currency.utils.FrameworkConstants.CurrencyDetails
import com.paypay.framework.exchange.currency.utils.FrameworkConstants.TableName

@Entity(tableName = TableName.CURRENCY_EXCHANGE_ENTITY)
data class CurrencyData(
    @PrimaryKey
    @ColumnInfo(name = CurrencyDetails.Column.CURRENCY_CODE) val currencyCode: String,
    @ColumnInfo(name = CurrencyDetails.Column.CURRENCY_VALUE) var currencyValue: Double?,
    @ColumnInfo(name = CurrencyDetails.Column.CURRENCY_NAME) val currencyName: String)