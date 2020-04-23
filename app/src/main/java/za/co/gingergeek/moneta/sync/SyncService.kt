package za.co.gingergeek.moneta.sync

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import za.co.gingergeek.moneta.MainApplication
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.net.OpenExchangeRatesAPI
import za.co.gingergeek.moneta.helpers.SharedPreferenceHelper
import javax.inject.Inject

class SyncService : IntentService("SyncService") {

    var api: OpenExchangeRatesAPI? = null
        @Inject set

    override fun onCreate() {
        super.onCreate()
        (application as MainApplication).applicationComponent?.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        fetchData()
    }

    private fun fetchData() {
        api?.fetchCurrencies {}
        api?.fetchExchangeRates {}
        SharedPreferenceHelper.saveLastSyncedTimestamp(this, System.currentTimeMillis())
    }
}