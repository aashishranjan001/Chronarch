package com.aashish.writetime.home.presentation

import com.aashish.writetime.common.domain.model.DurationType
import kotlin.time.Duration

data class HomeUiState(
    val currentDateCreditFocusPoints: Int = 0,
    val currentDateDebitFocusPoints: Int = 0,
    val totalSessionsStarted: Int = 0,
    val totalSessionsCompleted: Int = 0,
    val totalSessionsCancelled: Int = 0,
    val streaksCount: Int = 0,
    val activeTimer: ActiveTimer? = null,
    val selectedNewTimerDurationType: DurationType = DurationType.ShortDuration,
    val streakProgressFraction: Double = 0.0 // todo: revise logic for streak computation and remove this field
)

data class ActiveTimer(
    val sessionId: Long,
    val durationRemainingInSeconds: Long,
    val duration: Duration
)