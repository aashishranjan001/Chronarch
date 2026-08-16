package com.aashish.writetime.home.presentation

import com.aashish.writetime.common.domain.model.DurationType

sealed class HomeEvent {
    data object StartTimer: HomeEvent()
    data object CancelTimer: HomeEvent()

    data class SelectNewTimerDurationType(val durationType: DurationType): HomeEvent()
}