package za.co.gingergeek.moneta.room

import androidx.room.*
import za.co.gingergeek.moneta.models.Currency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(currency: Currency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(currencyList: List<Currency>)

    @Query("SELECT * FROM Currency")
    fun loadAll(): List<Currency>

    @Query("SELECT * FROM Currency WHERE isoCode == :isoCode")
    fun findByIsoCode(isoCode: String): Currency?

    @Delete
    fun delete(currency: Currency)
}