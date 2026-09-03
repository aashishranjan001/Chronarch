package com.aashish.chronarch.home.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.IntentCompat
import com.aashish.chronarch.home.domain.model.LatestTimerSession
import com.aashish.chronarch.home.domain.usecase.EndTimerSessionUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionClickReceiver: BroadcastReceiver() {

    @Inject lateinit var endTimerSessionUseCase: EndTimerSessionUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val pendingResult = goAsync()

            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                try {
                    val latestTimerSession = IntentCompat.getParcelableExtra(it, INTENT_EXTRA_LATEST_SESSION,
                        LatestTimerSession::class.java)

                    latestTimerSession?.let { session ->
                        endTimerSessionUseCase(
                            sessionId = session.id,
                            idealCompletionTime = session.idealEndTime,
                            durationType = session.durationType,
                            streakProgressFraction = session.runningStreakProgressionFraction
                        )
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }

    companion object {
        const val INTENT_EXTRA_LATEST_SESSION = "latest_timer_session"

        fun getIntent(context: Context, latestTimerSession: LatestTimerSession): Intent {
            return Intent(context, NotificationActionClickReceiver::class.java).apply {
                putExtra(INTENT_EXTRA_LATEST_SESSION, latestTimerSession)
            }
        }
    }
}