package com.aashish.chronarch.common.domain.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

sealed class DurationType(val duration: Duration) {
    object ShortDuration: DurationType(duration = 30.minutes)
    object LongDuration: DurationType(duration = 60.minutes)

    companion object {
        fun fromDuration(durationMillis: Long): DurationType {
            return when(durationMillis.milliseconds) {
                30.minutes -> ShortDuration
                60.minutes -> LongDuration
                else -> throw IllegalArgumentException("Non-standard timer duration")
            }
        }
    }
}