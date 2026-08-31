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
import com.aashish.chronarch.home.domain.usecase.EndTimerSessionUseCase
import com.aashish.chronarch.home.domain.usecase.GetLatestTimerSessionUseCase
import com.aashish.chronarch.home.notification.TimerNotificationManager
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
class TimerForegroundService: Service() {

    private var serviceScope: CoroutineScope? = null
    private var sessionJob: Job? = null

    @Inject
    lateinit var timerNotificationManager: TimerNotificationManager

    @Inject
    lateinit var latestTimerSessionUseCase: GetLatestTimerSessionUseCase

    @Inject
    lateinit var endTimerSessionUseCase: EndTimerSessionUseCase

    override fun onCreate() {
        super.onCreate()
        serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

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
        sessionJob = serviceScope?.launch {
            latestTimerSessionUseCase().collectLatest { latestSession ->
                if (latestSession != null && latestSession.sessionEndTime == null) { // latest session is available and has not ended => start timer service
                    updateNotificationTimerContent(latestSession.idealEndTime, latestSession.durationType.duration)
                } else {
                    stopSelf()
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        sessionJob?.cancel()
        sessionJob = null
        serviceScope?.cancel()
        serviceScope = null
        super.onDestroy()
    }

    private suspend fun updateNotificationTimerContent(
        completionTime: Instant,
        totalDuration: kotlin.time.Duration
    ) {
        val totalDurationInSeconds = totalDuration.inWholeSeconds
        val tapPendingIntent = getTapPendingIntent()
        val stopPendingIntent = getStopPendingIntent()

        while (Instant.now() <= completionTime) {
            timerNotificationManager.safePostActiveTimerNotification(
                durationRemainingInSeconds = Duration.between(Instant.now(), completionTime)
                    .toCeilSeconds(),
                totalDurationInSeconds = totalDurationInSeconds,
                completionTime = completionTime,
                tapPendingIntent = tapPendingIntent,
                stopPendingIntent = stopPendingIntent
            )
            delay(1.seconds)
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        timerNotificationManager.safePostTimerCompletedNotification(
            sessionDurationMins = totalDuration.inWholeMinutes, tapPendingIntent = tapPendingIntent
        )
        stopSelf()
    }

    private fun getTapPendingIntent() = PendingIntent.getActivity(
        this,
        1001,
        MainActivity.getIntent(this),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun getStopPendingIntent(): PendingIntent {
        return getTapPendingIntent() // temp stub. todo: change this with stop pending intent
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, TimerForegroundService::class.java)
    }
}