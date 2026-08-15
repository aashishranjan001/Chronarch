package com.aashish.writetime.weekoverview.domain.model


data class WeeklySessionOverview(
    val shortSessionsOverview: SessionCompletionCountOverview,
    val longSessionsOverview: SessionCompletionCountOverview,
    val allSessionsOverview: SessionCompletionCountOverview
)

data class SessionCompletionCountOverview(
    val successfulCompletionCount: Int,
    val cancelledCount: Int
) {
    val total: Int
        get() = successfulCompletionCount + cancelledCount
}