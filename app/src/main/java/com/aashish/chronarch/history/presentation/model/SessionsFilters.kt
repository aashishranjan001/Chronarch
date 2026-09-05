package com.aashish.chronarch.history.presentation.model

data class SessionsFilters(
    val appliedDurationTypeFilters: Set<filterOption.SessionFilter.DurationType> = emptySet(),
    val appliedCompletionStatusFilters: Set<filterOption.SessionFilter.CompletionStatus> = emptySet(),
    val appliedDateFilter: filterOption.Date? = null
)