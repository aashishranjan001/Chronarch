package com.aashish.chronarch.home.presentation

import com.aashish.chronarch.common.domain.model.DurationType

sealed class HomeEvent {
    data object StartTimer: HomeEvent()
    data object CancelTimerClick: HomeEvent()

    data class SelectNewTimerDurationType(val durationType: DurationType): HomeEvent()
    data object CancelTimerConfirmed: HomeEvent()
    data object CancelTimerDismissed: HomeEvent()

    data object TimerCompletedDialogDismiss: HomeEvent()
}

sealed class HomeUiEffect {
    data object ShowTimerCancelledSnackbar: HomeUiEffect()
}