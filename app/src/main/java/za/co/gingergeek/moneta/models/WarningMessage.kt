package za.co.gingergeek.moneta.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WarningMessage(@PrimaryKey val isoCode: String, val rate: Float?)