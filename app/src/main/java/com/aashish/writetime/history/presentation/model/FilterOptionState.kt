package com.aashish.writetime.history.presentation.model

sealed class FilterOptionState {
    data class SelectionFilterOptionState(
        val option: FilterOption,
        val isSelected: Boolean
    ) : FilterOptionState()

    data class DateFilterOptionState(
        val option: FilterOption,
    ) : FilterOptionState()
}