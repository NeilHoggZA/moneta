package za.co.gingergeek.moneta.models

import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedExchangeRate(
    @PrimaryKey @Bindable val isoCode: String,
    val minimumWarningRate: Float,
    @Bindable var exchangeRate: Float = 0f
) : Viewable() {
    override fun getType(): Int = typeStoredExchangeRate
}