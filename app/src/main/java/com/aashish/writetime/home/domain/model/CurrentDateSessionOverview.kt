package com.aashish.writetime.home.domain.model

import com.aashish.writetime.common.domain.model.DurationType

data class SessionsOverview(
    val successfulCompletionCount: Int,
    val cancelledCount: Int,
    val totalStreaks: Int,
    val activeSession: ActiveSessionInfo?,
    val currentStreakProgressionFraction: Double
)

data class ActiveSessionInfo(
    val id: Long,
    val durationType: DurationType,
    val durationRemainingSeconds: Long
)