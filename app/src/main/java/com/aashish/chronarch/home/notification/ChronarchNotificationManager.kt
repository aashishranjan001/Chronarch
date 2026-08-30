package com.aashish.chronarch.home.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.aashish.chronarch.R
import kotlin.time.Duration

class ChronarchNotificationManager(private val context: Context) {

    private val notificationManagerCompat = NotificationManagerCompat.from(context)

    init {
        createTimerNotificationChannel()
    }

    private fun createTimerNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(
            TIMER_NOTIFICATION_CHANNEL_ID,
            TIMER_NOTIFICATION_CHANNEL_NAME,
            importance
        ).apply {
            description = context.getString(R.string.timer_notification_channel_description)
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(mChannel)
    }

    private fun canPostNotificationToChannel(channelId: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Check runtime permission (Android 13+)
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) return false
        }

        if (!notificationManagerCompat.areNotificationsEnabled()) { // Check global app-level notification toggle (covers Android 12 & below)
            return false
        }

        val channel = notificationManagerCompat.getNotificationChannel(channelId)
        if (channel != null && channel.importance == NotificationManager.IMPORTANCE_NONE) return false

        return true
    }

    @SuppressLint("MissingPermission")
    fun showActiveTimerNotification(
        notificationId: Int,
        remainingTime: String,
        totalTime: String,
        tapPendingIntent: PendingIntent,
        stopPendingIntent: PendingIntent
    ) {
        if (!canPostNotificationToChannel(TIMER_NOTIFICATION_CHANNEL_ID)) return

        val notification = NotificationCompat.Builder(context, TIMER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_notification)
            .setContentTitle(context.getString(R.string.active_timer_notification_title))
            .setContentText(
                context.getString(
                    R.string.active_timer_notification_content,
                    remainingTime,
                    totalTime
                )
            )
            .setContentIntent(tapPendingIntent)
            .addAction(
                0,
                context.getString(R.string.timer_notification_stop_action_text),
                stopPendingIntent
            )
            .setAutoCancel(true)
            .build()

        notificationManagerCompat.notify(notificationId, notification)
    }

    @SuppressLint("MissingPermission")
    fun showTimerCompletedNotification(
        notificationId: Int,
        sessionDuration: Duration,
        tapPendingIntent: PendingIntent
    ) {
        if (!canPostNotificationToChannel(TIMER_NOTIFICATION_CHANNEL_ID)) return

        val notification = NotificationCompat.Builder(context, TIMER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_notification)
            .setContentTitle(context.getString(R.string.stopped_timer_notification_title))
            .setContentText(
                context.getString(
                    R.string.session_completed_message,
                    sessionDuration.inWholeMinutes
                )
            )
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManagerCompat.notify(notificationId, notification)
    }

    fun dismissNotification(notificationId: Int) {
        notificationManagerCompat.cancel(notificationId)
    }

    companion object {
        private const val TIMER_NOTIFICATION_CHANNEL_ID = "timer_notification"
        private const val TIMER_NOTIFICATION_CHANNEL_NAME = "Timer notifications"
    }
}