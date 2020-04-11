package za.co.gingergeek.moneta.room

import androidx.room.Database
import androidx.room.RoomDatabase
import za.co.gingergeek.moneta.models.*

@Database(entities = [Currency::class, ExchangeRate::class,
    ExchangeRateHistory::class, SavedExchangeRate::class, WarningMessage::class],
    version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun exchangeRateHistoryDao(): ExchangeRateHistoryDao
    abstract fun savedExchangeRatesDao(): SavedExchangeRatesDao
    abstract fun warningMessageDao(): WarningMessageDao
}