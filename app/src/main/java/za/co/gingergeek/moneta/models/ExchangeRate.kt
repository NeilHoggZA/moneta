package za.co.gingergeek.moneta.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExchangeRate(@PrimaryKey val isoCode: String, val exchangeRate: Float)