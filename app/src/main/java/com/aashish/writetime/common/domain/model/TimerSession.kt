package com.aashish.writetime.common.domain.model

import java.time.Instant


data class TimerSession(
    val id: Long,
    val startTime: Instant,
    val endTime: Instant?,
    val durationType: DurationType,
    val streakProgressFraction: Double
) {
    val associatedFocusPoints
        get() = when(durationType) {
            DurationType.LongDuration -> 2
            DurationType.ShortDuration -> 1
        }

    val idealCompletionTime: Instant
        get() = startTime.plusMillis(durationType.duration.inWholeMilliseconds)
}