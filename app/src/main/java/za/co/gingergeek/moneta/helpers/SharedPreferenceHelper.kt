package za.co.gingergeek.moneta.helpers

import android.content.Context
import androidx.preference.PreferenceManager

object SharedPreferenceHelper {

    private const val currenciesLastUpdated = "currencies_last_updated"
    private const val exchangeRatesLastUpdated = "exchange_rates_last_updated"
    private const val lastSynced = "last_synced"

    fun getCurrenciesLastUpdatedTimestamp(context: Context?): Long {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getLong(currenciesLastUpdated, -1)
    }

    fun saveCurrenciesLastUpdatedTimestamp(context: Context?, updated: Long) {
        val sharedPreferencesEditor =
            PreferenceManager.getDefaultSharedPreferences(context).edit()
        sharedPreferencesEditor.putLong(currenciesLastUpdated, updated)
        sharedPreferencesEditor.apply()
    }

    fun getExchangeRatesLastUpdatedTimestamp(context: Context?): Long {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getLong(exchangeRatesLastUpdated, -1)
    }

    fun saveExchangeRatesLastUpdatedTimestamp(context: Context?, updated: Long) {
        val sharedPreferencesEditor =
            PreferenceManager.getDefaultSharedPreferences(context).edit()
        sharedPreferencesEditor.putLong(exchangeRatesLastUpdated, updated)
        sharedPreferencesEditor.apply()
    }

    fun getLastSyncedTimestamp(context: Context?): Long {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getLong(lastSynced, -1)
    }

    fun saveLastSyncedTimestamp(context: Context?, updated: Long) {
        val sharedPreferencesEditor =
            PreferenceManager.getDefaultSharedPreferences(context).edit()
        sharedPreferencesEditor.putLong(lastSynced, updated)
        sharedPreferencesEditor.apply()
    }
}