package com.aashish.writetime.history.presentation.model

import java.time.LocalDate

sealed class FilterOptionType {
    data class Checkbox(
        val option: FilterOption,
        val isSelected: Boolean
    ) : FilterOptionType()

    data class Radio(val option: FilterOption, val isSelected: Boolean): FilterOptionType()
    data class DateRange(val startDate: LocalDate?, val endDate: LocalDate?): FilterOptionType()
}