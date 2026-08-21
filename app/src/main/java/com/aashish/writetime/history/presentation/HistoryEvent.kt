package com.aashish.writetime.history.presentation

sealed class HistoryEvent {
    data class TabSelect(val selectedTabIndex: Int): HistoryEvent()
}