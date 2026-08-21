package com.aashish.writetime.history.domain.model

import com.aashish.writetime.common.domain.model.DurationType
import java.time.Instant
import kotlin.time.Duration

data class EndedTimerSession(
    val id: Long,
    val startTime: Instant,
    val durationType: DurationType,
    val runDuration: Duration
)