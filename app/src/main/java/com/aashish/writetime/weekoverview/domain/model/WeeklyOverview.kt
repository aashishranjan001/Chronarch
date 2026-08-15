package com.aashish.writetime.weekoverview.domain.model

data class WeeklyOverview(
    val earningOverview: WeeklyEarningOverview,
    val sessionOverview: WeeklySessionOverview
)