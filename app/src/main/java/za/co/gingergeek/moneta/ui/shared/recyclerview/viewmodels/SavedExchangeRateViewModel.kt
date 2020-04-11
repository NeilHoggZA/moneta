package za.co.gingergeek.moneta.ui.shared.recyclerview.viewmodels

import androidx.databinding.BaseObservable
import za.co.gingergeek.moneta.models.SavedExchangeRate

data class SavedExchangeRateViewModel(val savedExchangeRate: SavedExchangeRate): BaseObservable() {
    val isoCode: String = savedExchangeRate.isoCode
    val isoCodeText: String = "USD\n${savedExchangeRate.isoCode}"
    var exchangeRate: String = savedExchangeRate.exchangeRate.toString();
}