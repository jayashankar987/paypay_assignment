package com.paypay.framework.exchange.currency.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.utils.FrameworkConstants.TableName

@Dao
interface IExchangeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyExchangeRates(vararg currencyData: CurrencyData)

    @Query("SELECT * FROM ${TableName.CURRENCY_EXCHANGE_ENTITY}")
    fun getAllCurrencyExchangeRates(): List<CurrencyData>
}