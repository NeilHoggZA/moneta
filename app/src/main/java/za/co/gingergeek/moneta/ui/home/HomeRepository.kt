package za.co.gingergeek.moneta.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.co.gingergeek.moneta.models.SavedExchangeRate
import za.co.gingergeek.moneta.ui.BaseRepository

class HomeRepository(val context: Context): BaseRepository(context) {

    fun loadStoredCurrencies(storedRates: MutableLiveData<List<SavedExchangeRate>>) {
        launch {
            val storedCurrencies = withContext(Dispatchers.IO) {
                val storedCurrencies = database?.savedExchangeRatesDao()?.loadAll() ?: listOf()
                storedCurrencies.forEach {
                    val latestExchangeRate = database?.exchangeRateDao()?.findByISOCode(it.isoCode)
                    it.exchangeRate = latestExchangeRate?.exchangeRate ?: 0f
                }

                storedCurrencies
            }

            storedRates.value = storedCurrencies
        }
    }


    fun deleteStoredExchangeRate(isoCode: String, completedCallback: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) { // maybe this coroutine should go
            withContext(Dispatchers.IO) {
                database?.savedExchangeRatesDao()?.delete(isoCode)
                database?.warningMessageDao()?.delete(isoCode)
            }
            completedCallback.invoke()
        }
    }
}