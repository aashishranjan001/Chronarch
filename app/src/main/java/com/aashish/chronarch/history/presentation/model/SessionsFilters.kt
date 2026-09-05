package com.aashish.chronarch.history.presentation.model

data class SessionsFilters(
    val appliedDurationTypeFilters: Set<FilterOption.SessionFilter.DurationType> = emptySet(),
    val appliedCompletionStatusFilters: Set<FilterOption.SessionFilter.CompletionStatus> = emptySet(),
    val appliedDateFilter: FilterOption.Date? = null
)