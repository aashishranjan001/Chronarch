package com.aashish.chronarch.weekoverview.domain.model

import com.aashish.chronarch.common.domain.model.EarningOverview
import java.time.DayOfWeek

data class WeekEarningOverview(
    val totalEarningOverview: EarningOverview,
    val dayWiseEarningOverview: Map<DayOfWeek, EarningOverview?>
)