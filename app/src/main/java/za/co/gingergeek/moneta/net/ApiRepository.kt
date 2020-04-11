package za.co.gingergeek.moneta.net

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.ui.BaseRepository
import za.co.gingergeek.moneta.helpers.NotificationHelper
import za.co.gingergeek.moneta.helpers.SharedPreferenceHelper
import za.co.gingergeek.moneta.models.*
import za.co.gingergeek.moneta.models.Currency
import za.co.gingergeek.moneta.models.events.SyncCompletedEvent
import java.util.*

class ApiRepository(val context: Context) : BaseRepository(context) {

    fun storeCurrencies(currencies: Map<String, String>?) {
        val map = currencies ?: mapOf()
        launch(Dispatchers.IO) {
            for ((key, value) in map) {
                database?.currencyDao()?.add(Currency(key, value))
            }
        }
        SharedPreferenceHelper.saveCurrenciesLastUpdatedTimestamp(
            context,
            System.currentTimeMillis()
        )
    }

    fun storeExchangeRates(exchangeRates: Map<String, Float>?) {
        launch {
            withContext(Dispatchers.IO) {
                val map = exchangeRates ?: mapOf()
                for ((key, value) in map) {
                    database?.exchangeRateDao()?.add(ExchangeRate(key, value))
                    database?.exchangeRateHistoryDao()?.add(
                        ExchangeRateHistory(
                            UUID.randomUUID().toString(),
                            System.currentTimeMillis(), key, value
                        )
                    )
                }

                val savedExchangeRates = database?.savedExchangeRatesDao()?.loadAll() ?: listOf()
                savedExchangeRates.forEach { savedRate ->
                    val updatedRate = map[savedRate.isoCode] ?: 0f
                    savedRate.exchangeRate = updatedRate
                    database?.savedExchangeRatesDao()?.add(savedRate)
                    sendWarningIfRequired(savedRate, updatedRate)
                }
            }

            EventBus.getDefault().post(SyncCompletedEvent())
        }

        SharedPreferenceHelper.saveExchangeRatesLastUpdatedTimestamp(
            context, System.currentTimeMillis()
        )
    }

    private fun sendWarningIfRequired(
        savedRate: SavedExchangeRate,
        updatedRate: Float
    ) {
        val warningMessageDao = database?.warningMessageDao()
        val previouslyNotifiedMessage =
            warningMessageDao?.loadMessageForCode(savedRate.isoCode)
        if (updatedRate > savedRate.minimumWarningRate
            && (previouslyNotifiedMessage == null
                    || previouslyNotifiedMessage.rate != updatedRate)
        ) {
            showNotification(context, savedRate)
            warningMessageDao?.add(WarningMessage(savedRate.isoCode, updatedRate))
        }
    }

    private fun showNotification(context: Context, savedRate: SavedExchangeRate) {
        val title = context.getString(R.string.warning_notification_title, savedRate.isoCode)
        val description =
            context.getString(R.string.warning_notification_description, savedRate.isoCode)
        val longDescription = String.format(
            context.getString(R.string.warning_notification_long_description),
            savedRate.isoCode,
            savedRate.exchangeRate.toString()
        )
        NotificationHelper.showAlertNotification(
            context, savedRate.isoCode,
            title, description, longDescription,
            "exchange_rate_warning"
        )
    }
}