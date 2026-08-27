package com.aashish.chronarch.home.presentation

import com.aashish.chronarch.common.domain.model.DurationType
import java.time.Instant
import kotlin.time.Duration

data class HomeUiState(
    val currentDateTotalCreditFocusPoints: Int = 0,
    val currentDateDebitFocusPoints: Int = 0,
    val totalSessionsStarted: Int = 0,
    val totalSessionsCompleted: Int = 0,
    val totalSessionsCancelled: Int = 0,
    val streaksCount: Int = 0,
    val activeTimer: ActiveTimer? = null,
    val selectedNewTimerDurationType: DurationType = DurationType.ShortDuration,
    val streakProgressFraction: Double = 0.0, // todo: revise logic for streak computation and remove this field
    val dialog: HomeDialog? = null,
    val showTimerCompletedDial: Boolean = false,
)

sealed class HomeDialog {
    data object TimerCancelConfirmation: HomeDialog()
    data class TimerFinishedInformation(val timerDurationMins: Long): HomeDialog()
}

data class ActiveTimer(
    val sessionId: Long,
    val durationRemainingInSeconds: Long,
    val duration: Duration,
    val idealEndTime: Instant
)