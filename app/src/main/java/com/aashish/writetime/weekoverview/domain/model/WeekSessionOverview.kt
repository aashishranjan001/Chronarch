package com.aashish.writetime.weekoverview.domain.model


data class WeekSessionOverview(
    val shortSessionsOverview: SessionCompletionCountOverview,
    val longSessionsOverview: SessionCompletionCountOverview,
)

data class SessionCompletionCountOverview(
    val successfulCompletionCount: Int,
    val cancelledCount: Int
) {
    val totalAttemptedSessionsCount: Int
        get() = successfulCompletionCount + cancelledCount
}