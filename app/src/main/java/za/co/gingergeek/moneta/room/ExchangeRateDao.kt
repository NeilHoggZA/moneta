package za.co.gingergeek.moneta.room

import androidx.room.*
import za.co.gingergeek.moneta.models.Currency
import za.co.gingergeek.moneta.models.ExchangeRate

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(exchangeRate: ExchangeRate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<ExchangeRate>)

    @Query("SELECT * FROM ExchangeRate")
    fun loadAll(): List<ExchangeRate>

    @Query("SELECT * FROM ExchangeRate WHERE isoCode = :code LIMIT 1")
    fun findByISOCode(code: String): ExchangeRate?

    @Delete
    fun delete(currency: ExchangeRate)
}