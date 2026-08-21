package com.aashish.writetime.history.presentation

sealed class HistoryEvent {
    data class TabSelect(val tabIndex: Int): HistoryEvent()
}

sealed class HistoryUiEffect {
    data class ScrollToTab(val tabIndex: Int): HistoryUiEffect()
}