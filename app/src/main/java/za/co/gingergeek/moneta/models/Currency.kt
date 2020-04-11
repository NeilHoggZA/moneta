package za.co.gingergeek.moneta.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(@PrimaryKey val isoCode: String, val name: String)