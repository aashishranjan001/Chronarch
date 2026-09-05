package com.aashish.chronarch.history.presentation.model

import java.time.LocalDate

sealed class FilterOptionItem {
    data class Checkbox(
        val option: filterOption,
        val isSelected: Boolean
    ) : FilterOptionItem()

    data class Radio(val option: filterOption, val isSelected: Boolean): FilterOptionItem()
    data class DateRange(val startDate: LocalDate?, val endDate: LocalDate?): FilterOptionItem()
}