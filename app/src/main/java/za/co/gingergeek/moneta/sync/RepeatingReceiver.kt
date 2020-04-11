package za.co.gingergeek.moneta.sync

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import za.co.gingergeek.moneta.helpers.SharedPreferenceHelper

class RepeatingReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val lastSync: Long = SharedPreferenceHelper.getLastSyncedTimestamp(context)
        val oneHourAgo = System.currentTimeMillis() - 1000 * 60 * 60 * 1
        if (lastSync < oneHourAgo || lastSync == -1L) {
            SyncService.enqueuePeriodicSync(context)
            SharedPreferenceHelper.saveLastSyncedTimestamp(context, System.currentTimeMillis())
        }
        scheduleAlarm(context)
    }

    companion object {
        fun scheduleAlarm(context: Context) {
            val receiverIntent = Intent(context, RepeatingReceiver::class.java)
            val sender = PendingIntent.getBroadcast(
                context,
                19000,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val time = SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, sender)
            } else {
                alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, time] = sender
            }
        }
    }
}