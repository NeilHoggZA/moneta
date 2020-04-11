package za.co.gingergeek.moneta.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import za.co.gingergeek.moneta.MainApplication

fun Context.isNetworkConnected(): Boolean {
    return (applicationContext as MainApplication).networkAvailable
}

@Suppress("DEPRECATION")
fun Context.isWifiConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
    if (connectivityManager is ConnectivityManager) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
                ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                ?: return false
            return networkInfo.isConnected
        }
    }
    return false
}