package za.co.gingergeek.moneta.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import za.co.gingergeek.moneta.MainActivity
import za.co.gingergeek.moneta.R

object NotificationHelper {
    fun showAlertNotification(
        context: Context,
        isoCode: String?,
        title: String?,
        description: String?,
        longDescription: String?,
        channelId: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(context, channelId)
        }
        val extras = Bundle()
        extras.putString("iso_code", isoCode)
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        notificationIntent.putExtras(extras)
        val intent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_notification
                )
            )
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(longDescription)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, builder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannel(
        context: Context,
        channelId: String
    ) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel =
            NotificationChannel(channelId, channelId, importance)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}