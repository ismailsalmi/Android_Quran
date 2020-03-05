package com.salmi.quran

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon

object NotificationBadge {
    private var notificationManager: NotificationManager? = null
    private val notificationID = 290
    @TargetApi(26)
    fun badge(context: Context, title: String) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val notifyIntent = Intent(context, MainPlayer::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
        }
        val notifyPendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val id = "some_channel_id"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, "Badge", importance)
        channel.setShowBadge(true)
        notificationManager!!.createNotificationChannel(channel)
        val notification = Notification.Builder(context, id)
                .setContentTitle(title)
                .setVisibility(Notification.VISIBILITY_PUBLIC)//show On Lock Screen
                .setSmallIcon(Icon.createWithResource(context, R.drawable.ic_pause_circle_black))
                .setBadgeIconType(Notification.BADGE_ICON_SMALL)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(notifyPendingIntent)
                .build()
        notificationManager!!.notify(this.notificationID, notification)
    }

    fun cancelNotification() {
        notificationManager!!.cancel(this.notificationID)
    }
}
