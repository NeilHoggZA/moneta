package za.co.gingergeek.moneta.ui.addcurrency

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.co.gingergeek.moneta.models.Currency
import za.co.gingergeek.moneta.models.SavedExchangeRate
import za.co.gingergeek.moneta.ui.BaseRepository

class AddCurrencyRepository(context: Context) : BaseRepository(context) {

    fun loadAllCurrencies(callback: (List<Currency>) -> Unit) {
        launch(Dispatchers.Main) {
            callback(withContext(Dispatchers.IO) {
                database?.currencyDao()?.loadAll() ?: listOf()
            }.sortedBy { it.isoCode })
        }
    }

    fun loadExchangeRateForIsoCode(isoCode: String, callback: (Float) -> Unit) {
        launch(Dispatchers.Main) {
            callback.invoke(withContext(Dispatchers.IO) {
                database?.exchangeRateDao()?.findByISOCode(isoCode)?.exchangeRate ?: 0f
            })
        }
    }

    fun storeMonitoredExchangeRate(currency: Currency, warningRate: Float) {
        launch(Dispatchers.IO) {
            database?.savedExchangeRatesDao()?.add(SavedExchangeRate(currency.isoCode, warningRate))
        }
    }
}