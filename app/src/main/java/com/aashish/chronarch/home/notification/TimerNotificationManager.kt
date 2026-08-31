package com.aashish.chronarch.home.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
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
import com.aashish.chronarch.common.ui.formatDurationHhMmSs

class TimerNotificationManager(private val context: Context) {

    private val notificationManagerCompat = NotificationManagerCompat.from(context)

    init {
        createTimerNotificationChannel()
    }

    private fun createTimerNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            importance
        ).apply {
            description = context.getString(R.string.timer_notification_channel_description)
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(mChannel)
    }

    fun canPostNotificationToChannel(channelId: String): Boolean {
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
    fun safePostActiveTimerNotification(
        durationRemainingInSeconds: Long,
        totalDurationInSeconds: Long,
        tapPendingIntent: PendingIntent,
        stopPendingIntent: PendingIntent
    ) {
        if (!canPostNotificationToChannel(NOTIFICATION_CHANNEL_ID)) return

        val activeTimerNotification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_active_notification)
            .setContentTitle(context.getString(R.string.active_timer_notification_title))
            .setContentText(
                context.getString(
                    R.string.active_timer_notification_content,
                    formatDurationHhMmSs(durationRemainingInSeconds),
                    formatDurationHhMmSs(totalDurationInSeconds)
                )
            )
            .setContentIntent(tapPendingIntent)
            .addAction(
                0,
                context.getString(R.string.timer_notification_stop_action_text),
                stopPendingIntent
            )
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

        // suppressLint because canPostNotificationToChannel already makes the permission check
        notificationManagerCompat.notify(NOTIFICATION_ID, activeTimerNotification)
    }

    @SuppressLint("MissingPermission")
    fun safePostTimerCompletedNotification(
        sessionDurationMins: Long,
        tapPendingIntent: PendingIntent
    ) {
        if (!canPostNotificationToChannel(NOTIFICATION_CHANNEL_ID)) return

        val timerCompletedNotification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_completed_notification)
            .setContentTitle(context.getString(R.string.stopped_timer_notification_title))
            .setContentText(
                context.getString(
                    R.string.session_completed_message,
                    sessionDurationMins
                )
            )
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(false)
            .build()

        // suppressLint because canPostNotificationToChannel already makes the permission check
        notificationManagerCompat.notify(NOTIFICATION_ID, timerCompletedNotification)
    }
    fun getPlaceholderNotification(
        tapPendingIntent: PendingIntent
    ): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_active_notification)
            .setContentTitle(context.getString(R.string.active_timer_notification_title))
            .setContentText(context.getString(R.string.timer_starting))
            .setContentIntent(tapPendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "timer_notification"
        private const val NOTIFICATION_CHANNEL_NAME = "Timer notifications"
        const val NOTIFICATION_ID = 1001
    }
}