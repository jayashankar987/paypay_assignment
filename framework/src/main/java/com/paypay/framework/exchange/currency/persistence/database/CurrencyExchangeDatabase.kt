package com.paypay.framework.exchange.currency.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.Converters
import com.paypay.framework.exchange.currency.persistence.dao.IExchangeDao

@Database(entities = [CurrencyData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CurrencyExchangeDatabase : RoomDatabase() {

    abstract fun getExchangeDao(): IExchangeDao

    companion object AppMigration {
        //no Migration needed as of now but might need incase of any update to db storage
        /*val migration_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                TODO("Not yet implemented")
            }
        }*/
    }
}