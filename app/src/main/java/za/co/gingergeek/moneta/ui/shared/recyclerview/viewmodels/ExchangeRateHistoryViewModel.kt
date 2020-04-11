package za.co.gingergeek.moneta.ui.shared.recyclerview.viewmodels

import androidx.databinding.BaseObservable
import za.co.gingergeek.moneta.models.ExchangeRateHistory
import java.text.SimpleDateFormat
import java.util.*

class ExchangeRateHistoryViewModel(exchangeRateHistory: ExchangeRateHistory) : BaseObservable() {
    var time: String = SimpleDateFormat("yy/MM/dd HH:mm", Locale.getDefault()).format(exchangeRateHistory.timestamp)
    val isoCode: String = exchangeRateHistory.isoCode
    val exchangeRate: String = exchangeRateHistory.exchangeRate.toString()
}