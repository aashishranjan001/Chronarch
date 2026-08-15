package com.aashish.writetime.weekoverview.domain.model

import com.aashish.writetime.common.domain.model.EarningOverview
import java.time.DayOfWeek

data class WeeklyEarningOverview(
    val totalEarningOverview: EarningOverview,
    val dayWiseEarningOverview: Map<DayOfWeek, EarningOverview?>
)