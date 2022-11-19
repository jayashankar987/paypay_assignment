package com.paypay.framework.exchange.currency.di

import android.content.Context
import androidx.room.Room
import com.paypay.framework.exchange.currency.persistence.dao.IExchangeDao
import com.paypay.framework.exchange.currency.persistence.database.CurrencyExchangeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule() {

    @Provides
    internal fun provideCurrencyExchangeDatabase(@ApplicationContext context: Context): CurrencyExchangeDatabase {
        return Room.databaseBuilder(context, CurrencyExchangeDatabase::class.java, "cx.db").build()
    }

    @Provides
    fun providesExchangeDao(currencyExchangeDatabase: CurrencyExchangeDatabase): IExchangeDao {
        return currencyExchangeDatabase.getExchangeDao()
    }
}