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
        runForeground()
        fetchData()
    }

    private fun runForeground() {
        val channelId = "sync_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                getString(R.string.sync_message),
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.sync_title))
            .setContentText(getString(R.string.sync_message))
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSound(null)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)).build()

        startForeground(1, notification)
    }

    private fun fetchData() {
        api?.fetchCurrencies {}
        api?.fetchExchangeRates {}
        SharedPreferenceHelper.saveLastSyncedTimestamp(this, System.currentTimeMillis())
    }
}