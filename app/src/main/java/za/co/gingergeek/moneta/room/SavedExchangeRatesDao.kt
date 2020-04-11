package za.co.gingergeek.moneta.room

import androidx.room.*
import za.co.gingergeek.moneta.models.Currency
import za.co.gingergeek.moneta.models.ExchangeRate
import za.co.gingergeek.moneta.models.SavedExchangeRate

@Dao
interface SavedExchangeRatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(exchangeRate: SavedExchangeRate)

    @Query("SELECT * FROM SavedExchangeRate")
    fun loadAll(): List<SavedExchangeRate>

    @Query("DELETE FROM SavedExchangeRate WHERE isoCode == :isoCode")
    fun delete(isoCode: String)
}