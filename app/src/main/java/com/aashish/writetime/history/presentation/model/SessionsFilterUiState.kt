package com.aashish.writetime.history.presentation.model

data class SessionsFilterUiState(
    val selectedFilterCategory: FilterCategory.SessionFilter? = null,
    val appliedDurationTypeFilters: Set<FilterOption.SessionFilter.DurationType> = emptySet(),
    val appliedCompletionStatusFilters: Set<FilterOption.SessionFilter.CompletionStatus> = emptySet(),
    val appliedDateFilter: FilterOption.SessionFilter.Date? = null
)