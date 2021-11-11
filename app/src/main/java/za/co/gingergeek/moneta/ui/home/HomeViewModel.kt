package za.co.gingergeek.moneta.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.models.SavedExchangeRate
import za.co.gingergeek.moneta.helpers.SharedPreferenceHelper
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel()   {
    private var context: Context? = null
    private lateinit var homeRepository: HomeRepository

    val savedRates: MutableLiveData<List<SavedExchangeRate>> = MutableLiveData<List<SavedExchangeRate>>()
    val lastUpdated: MutableLiveData<String> = MutableLiveData<String>()

    fun initialize(context: Context) {
        this.context = context
        this.homeRepository = HomeRepository(context)

        populateCurrencies()
        populateLastUpdated(context)
    }

    fun refreshData() {
        populateCurrencies()
        populateLastUpdated(context)
    }

    fun findSavedRate(isoCode: String): SavedExchangeRate? {
        return savedRates.value?.find { it.isoCode == isoCode }
    }

    private fun populateCurrencies() {
        homeRepository.loadStoredCurrencies(savedRates)
    }

    private fun populateLastUpdated(context: Context?) {
        val lastUpdatedMillis = SharedPreferenceHelper.getExchangeRatesLastUpdatedTimestamp(context)
        val lastUpdatedText = context?.getString(R.string.home_screen_last_updated_prefix) ?: ""
        val pattern = if (isToday(lastUpdatedMillis)) "HH:mm" else "MM/dd HH:mm"

        lastUpdated.value =
            "$lastUpdatedText ${SimpleDateFormat(pattern, Locale.getDefault()).format(
                lastUpdatedMillis
            )}"
    }

    private fun isToday(lastUpdatedMillis: Long): Boolean {
        val calendar = Calendar.getInstance()
        val todayCalendar = Calendar.getInstance()
        calendar.time = Date(lastUpdatedMillis)
        return todayCalendar.get(Calendar.DATE).equals(calendar.get(Calendar.DATE))
    }

    fun deleteStoredExchangeRate(isoCode: String, function: () -> Unit) {
        homeRepository.deleteStoredExchangeRate(isoCode, function)
    }
}