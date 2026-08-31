package com.aashish.chronarch.home.domain.model

import com.aashish.chronarch.common.domain.model.DurationType
import java.time.Instant

data class LatestTimerSession(
    val id: Long,
    val durationType: DurationType,
    val idealEndTime: Instant,
    val sessionEndTime: Instant?,
    val runningStreakProgressionFraction: Double
)