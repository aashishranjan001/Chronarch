package com.aashish.writetime.history.presentation

import com.aashish.writetime.history.presentation.model.FilterCategory
import com.aashish.writetime.history.presentation.model.FilterOptionItem

sealed class HistoryEvent {
    data class TabSelect(val tabIndex: Int): HistoryEvent()
    data class SaveSelectedTab(val tabIndex: Int): HistoryEvent()

    data class FilterCategorySelected(val category: FilterCategory): HistoryEvent()
    data class FilterCheckboxOptionSelected(val optionItem: FilterOptionItem.Checkbox, val isSelected: Boolean): HistoryEvent()
    data class FilterRadioOptionSelected(val optionItem: FilterOptionItem.Radio): HistoryEvent()
    data object DateRangeFilterOptionClick: HistoryEvent()
    data object ClearDraftFilters: HistoryEvent()
    data object ResetAppliedFilters: HistoryEvent()
    data object ApplyDraftFilters: HistoryEvent()
    data object DismissFilterMenu: HistoryEvent()

    data object FilterMenuIconClick: HistoryEvent()

    data class DaterFilterApplied(val from: Long?, val to: Long?): HistoryEvent()
    data object DateRangePickerDismissed: HistoryEvent()

    data object RetryClick: HistoryEvent()

}

sealed class HistoryUiEffect {
    data class ScrollToTab(val tabIndex: Int): HistoryUiEffect()
}