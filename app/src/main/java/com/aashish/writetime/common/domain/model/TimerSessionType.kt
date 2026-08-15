package com.aashish.writetime.common.domain.model

sealed class TimerSessionType(val durationMins: Int) {
    object Short: TimerSessionType(durationMins = 30)
    object Long: TimerSessionType(durationMins = 60)

    companion object {
        fun fromDuration(durationMins: Int) {
            when(durationMins) {
                30 -> Short
                60 -> Long
                else -> throw IllegalArgumentException("Non-standard timer duration")
            }
        }
    }
}