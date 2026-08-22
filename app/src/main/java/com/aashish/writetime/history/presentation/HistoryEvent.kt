package com.aashish.writetime.history.presentation

import com.aashish.writetime.history.presentation.model.FilterCategory
import com.aashish.writetime.history.presentation.model.FilterOption

sealed class HistoryEvent {
    data class TabSelect(val tabIndex: Int): HistoryEvent()

    data class FilterCategorySelected(val category: FilterCategory): HistoryEvent()
    data class FilterOptionSelected(val option: FilterOption, val isSelected: Boolean): HistoryEvent()
    data object SessionsCustomDateRangeOptionClick: HistoryEvent()
    data object TransactionsCustomDateRangeOptionClick: HistoryEvent()
    data object ClearTransactionsFilter: HistoryEvent()
    data object ClearSessionsFilter: HistoryEvent()
    data object ApplySessionsFilter: HistoryEvent()
    data object ApplyTransactionsFilter: HistoryEvent()
    data object DismissSessionsFilterMenu: HistoryEvent()
    data object DismissTransactionsFilterMenu: HistoryEvent()

    data object SessionsTabFilterMenuClick: HistoryEvent()
    data object TransactionsTabFilterMenuClick: HistoryEvent()

    data class SessionsDaterFilterApplied(val from: Long, val to: Long): HistoryEvent()
    data class TransactionsDaterFilterApplied(val from: Long, val to: Long): HistoryEvent()
    data object DateRangePickerDismissed: HistoryEvent()

}

sealed class HistoryUiEffect {
    data class ScrollToTab(val tabIndex: Int): HistoryUiEffect()
}