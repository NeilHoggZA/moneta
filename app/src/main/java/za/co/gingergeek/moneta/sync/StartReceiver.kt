package za.co.gingergeek.moneta.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import za.co.gingergeek.moneta.helpers.SharedPreferenceHelper

class StartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val lastSync: Long = SharedPreferenceHelper.getLastSyncedTimestamp(context)
        val oneHourAgo = System.currentTimeMillis() - 1000 * 60 * 60 * 1
        if (lastSync < oneHourAgo || lastSync == -1L) {
            SyncJob.scheduleJob(context)
            SharedPreferenceHelper.saveLastSyncedTimestamp(context, System.currentTimeMillis())
        }
    }
}