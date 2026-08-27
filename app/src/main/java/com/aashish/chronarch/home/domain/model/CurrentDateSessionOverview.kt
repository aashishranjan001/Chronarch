package com.aashish.chronarch.home.domain.model

import com.aashish.chronarch.common.domain.model.DurationType
import java.time.Instant

data class SessionsOverview(
    val successfulCompletionCount: Int,
    val cancelledCount: Int,
    val totalStreaks: Int,
    val activeSession: ActiveSessionInfo?,
    val currentStreakProgressionFraction: Double
) {
    val totalStartedCount
        get() = successfulCompletionCount + cancelledCount
}

data class ActiveSessionInfo(
    val id: Long,
    val durationType: DurationType,
    val idealCompletionTime: Instant,
)