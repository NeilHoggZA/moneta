package za.co.gingergeek.moneta.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExchangeRateHistory(@PrimaryKey val id: String,
                               val timestamp: Long,
                               val isoCode: String,
                               val exchangeRate: Float): Viewable() {
    override fun getType(): Int = typeHistoricalExchangeRate
}

