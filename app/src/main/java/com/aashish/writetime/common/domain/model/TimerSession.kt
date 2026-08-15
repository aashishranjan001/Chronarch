package com.aashish.writetime.common.domain.model

import kotlin.time.Instant

data class TimerSession(
    val id: Long = 0,
    val startTime: Instant,
    val endTime: Instant?,
    val durationType: DurationType,
    val completionStatus: CompletionStatus,
    val streakProgressFraction: Double
)

enum class CompletionStatus {
    COMPLETED, NOT_COMPLETED
}