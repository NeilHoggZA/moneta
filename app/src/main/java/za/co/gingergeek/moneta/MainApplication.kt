package za.co.gingergeek.moneta

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import za.co.gingergeek.moneta.application.LifeCycleCallbacks
import za.co.gingergeek.moneta.di.ApplicationComponent
import za.co.gingergeek.moneta.di.ApplicationModule
import za.co.gingergeek.moneta.di.DaggerApplicationComponent
import za.co.gingergeek.moneta.helpers.SharedPreferenceHelper
import za.co.gingergeek.moneta.net.OpenExchangeRatesAPI
import za.co.gingergeek.moneta.sync.StartReceiver
import za.co.gingergeek.moneta.sync.SyncJob
import za.co.gingergeek.moneta.sync.SyncService
import javax.inject.Inject

class MainApplication : Application() {
    private val startupInterval: Long = 1000 * 5
    private val syncInterval: Long = 1000 * 60 * 15
    private val alarmInterval: Long = 1000 * 60 * 60 * 1
    private val handler = Handler()

    var api: OpenExchangeRatesAPI? = null
        @Inject set

    var applicationComponent: ApplicationComponent? = null
    var networkAvailable = false

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        registerCallbacks()

        // keeps track of the network state
        ConnectionStateMonitor(this) {
            networkAvailable = it
        }.enable()

        scheduleSync()

        applicationComponent?.inject(this)
    }

    private fun registerCallbacks() {
        registerActivityLifecycleCallbacks(LifeCycleCallbacks {
            if (it.state == LifeCycleCallbacks.ActivityState.DESTROYED) {
                if (!isForegrounded() && it.activity.javaClass == MainActivity::class.java) {
                    handler.removeCallbacks(handlerTask)
                    api?.dispose()
                }
            }
        })
    }

    private fun scheduleSync() {
        handler.removeCallbacks(handlerTask)

        val lastSync: Long = SharedPreferenceHelper.getLastSyncedTimestamp(this)
        val oneHourAgo = System.currentTimeMillis() - alarmInterval
        if (lastSync < oneHourAgo || lastSync == -1L) {
            SyncJob.scheduleJob(this)
        }

        handler.postDelayed(handlerTask, startupInterval)
    }

    private val handlerTask: Runnable = object : Runnable {
        override fun run() {
            if (isForegrounded()) {
                SyncJob.scheduleJob(this@MainApplication)
            }
            handler.postDelayed(this, syncInterval)
        }
    }

    private fun isForegrounded(): Boolean {
        val appProcessInfo = RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return (appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                || appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE)
    }

    private class ConnectionStateMonitor internal constructor(
        private val context: Context,
        private val callback: (Boolean) -> Unit
    ) :
        NetworkCallback() {
        private val networkRequest: NetworkRequest =
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()

        fun enable() {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(networkRequest, this)
        }

        override fun onAvailable(network: Network) {
            callback(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            callback(false)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            callback(false)
        }

    }
}