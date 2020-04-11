package za.co.gingergeek.moneta.room

import androidx.room.*
import za.co.gingergeek.moneta.models.Currency
import za.co.gingergeek.moneta.models.ExchangeRate
import za.co.gingergeek.moneta.models.SavedExchangeRate
import za.co.gingergeek.moneta.models.WarningMessage

@Dao
interface WarningMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(message: WarningMessage)

    @Query("SELECT * FROM WarningMessage WHERE isoCode == :isoCode")
    fun loadMessageForCode(isoCode: String): WarningMessage?

    @Query("DELETE FROM WarningMessage WHERE isoCode == :isoCode")
    fun delete(isoCode: String)
}