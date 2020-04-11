package za.co.gingergeek.moneta.ui.currencydetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.co.gingergeek.moneta.models.ExchangeRateHistory
import za.co.gingergeek.moneta.ui.BaseRepository

class CurrencyDetailRepository(context: Context) : BaseRepository(context) {

    fun populateExchangeRate(isoCode: String, exchangeRateDescription: MutableLiveData<String>, exchangeRate: MutableLiveData<String>) {
        launch(Dispatchers.Main) {
            val exchangeRateDetails = withContext(Dispatchers.IO) {
                database?.exchangeRateDao()?.findByISOCode(isoCode)
            }

            exchangeRate.value = exchangeRateDetails?.exchangeRate.toString()
            exchangeRateDescription.value = "USD/${exchangeRateDetails?.isoCode}"
        }
    }

    fun populateExchangeRateHistory(isoCode: String, exchangeRateHistory: MutableLiveData<List<ExchangeRateHistory>>) {
        launch(Dispatchers.Main) {
            exchangeRateHistory.value = withContext(Dispatchers.IO) {
                database?.exchangeRateHistoryDao()?.findByISOCode(isoCode)
            }
        }
    }
}