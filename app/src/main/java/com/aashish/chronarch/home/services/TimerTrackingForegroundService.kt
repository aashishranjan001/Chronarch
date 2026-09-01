package com.aashish.chronarch.home.services

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.aashish.chronarch.MainActivity
import com.aashish.chronarch.common.ui.toCeilSeconds
import com.aashish.chronarch.home.broadcast.NotificationActionClickReceiver
import com.aashish.chronarch.home.domain.model.LatestTimerSession
import com.aashish.chronarch.home.domain.usecase.EndTimerSessionUseCase
import com.aashish.chronarch.home.domain.usecase.GetLatestTimerSessionUseCase
import com.aashish.chronarch.home.notification.TimerNotificationManager
import com.aashish.chronarch.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class TimerTrackingForegroundService : Service() {

    private val serviceScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var sessionJob: Job? = null

    @Inject
    lateinit var timerNotificationManager: TimerNotificationManager

    @Inject
    lateinit var latestTimerSessionUseCase: GetLatestTimerSessionUseCase

    @Inject
    lateinit var endTimerSessionUseCase: EndTimerSessionUseCase

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ TimerNotificationManager.NOTIFICATION_ID,
            /* notification = */ timerNotificationManager.getPlaceholderNotification(getTapPendingIntent()),
            /* foregroundServiceType = */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE else 0
        )

        sessionJob?.cancel()
        sessionJob = serviceScope.launch {
            latestTimerSessionUseCase().collectLatest { latestSession ->

                // latest timer session is available and is either an active timer or finished timer
                if (latestSession != null && (latestSession.sessionEndTime == null || latestSession.sessionEndTime >= latestSession.idealEndTime)) {
                    runTimer(latestSession)
                }
                // stop once we've finished handling this session (active or otherwise)
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    private suspend fun runTimer(
        latestTimerSession: LatestTimerSession
    ) {
        val totalDurationInSeconds = latestTimerSession.durationType.duration.inWholeSeconds
        val tapPendingIntent = getTapPendingIntent()
        val stopPendingIntent = getStopPendingIntent(latestTimerSession)

        while (Instant.now() <= latestTimerSession.idealEndTime) {
            timerNotificationManager.safePostActiveTimerNotification(
                durationRemainingInSeconds = Duration.between(
                    Instant.now(),
                    latestTimerSession.idealEndTime
                )
                    .toCeilSeconds(),
                totalDurationInSeconds = totalDurationInSeconds,
                completionTime = latestTimerSession.idealEndTime,
                tapPendingIntent = tapPendingIntent,
                stopPendingIntent = stopPendingIntent
            )
            delay(1.seconds)
        }
        endTimerSessionUseCase(
            sessionId = latestTimerSession.id,
            idealCompletionTime = latestTimerSession.idealEndTime,
            durationType = latestTimerSession.durationType,
            streakProgressFraction = latestTimerSession.runningStreakProgressionFraction
        )
        stopForeground(STOP_FOREGROUND_REMOVE)
        timerNotificationManager.safePostTimerCompletedNotification(
            sessionDurationMins = latestTimerSession.durationType.duration.inWholeMinutes,
            tapPendingIntent = tapPendingIntent
        )
    }

    private fun getTapPendingIntent() = PendingIntent.getActivity(
        this,
        TAP_PENDING_INTENT_REQUEST_CODE,
        MainActivity.getIntent(this, Screen.Home),
         PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun getStopPendingIntent(latestTimerSession: LatestTimerSession): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            STOP_PENDING_INTENT_REQUEST_CODE,
            NotificationActionClickReceiver.getIntent(this, latestTimerSession),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, TimerTrackingForegroundService::class.java)
        const val TAP_PENDING_INTENT_REQUEST_CODE = 1001
        const val STOP_PENDING_INTENT_REQUEST_CODE = 1002
    }
}