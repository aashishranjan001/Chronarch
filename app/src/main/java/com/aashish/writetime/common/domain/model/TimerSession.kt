package com.aashish.writetime.common.domain.model

import java.time.Instant


data class TimerSession(
    val id: Long,
    val startTime: Instant,
    val endTime: Instant?,
    val durationType: DurationType,
    val streakProgressFraction: Double
) {
    val idealCompletionTime: Instant
        get() = startTime.plusMillis(durationType.duration.inWholeMilliseconds)
}