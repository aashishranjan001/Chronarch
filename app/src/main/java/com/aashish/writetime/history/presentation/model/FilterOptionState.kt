package com.aashish.writetime.history.presentation.model

import java.time.LocalDate

sealed class FilterOptionState {
    data class SelectionFilterOptionState(
        val option: FilterOption,
        val isSelected: Boolean
    ) : FilterOptionState()

    sealed class DateFilterOptionState: FilterOptionState() {
        data class FixedRange(val option: FilterOption, val isSelected: Boolean): DateFilterOptionState()
        data class CustomRange(val startDate: LocalDate?, val endDate: LocalDate?): DateFilterOptionState()
    }
}