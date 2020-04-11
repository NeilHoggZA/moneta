package za.co.gingergeek.moneta.room

import androidx.room.*
import za.co.gingergeek.moneta.models.Currency
import za.co.gingergeek.moneta.models.ExchangeRate
import za.co.gingergeek.moneta.models.ExchangeRateHistory

@Dao
interface ExchangeRateHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(exchangeRate: ExchangeRateHistory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<ExchangeRateHistory>)

    @Query("SELECT * FROM ExchangeRateHistory")
    fun loadAll(): List<ExchangeRateHistory>

    @Query("SELECT * FROM ExchangeRateHistory WHERE isoCode == :isoCode")
    fun findByISOCode(isoCode: String): List<ExchangeRateHistory>

    @Query("DELETE FROM ExchangeRateHistory WHERE isoCode == :isoCode")
    fun delete(isoCode: String)
}