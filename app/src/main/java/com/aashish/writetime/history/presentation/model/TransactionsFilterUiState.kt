package com.aashish.writetime.history.presentation.model

data class TransactionsFilterUiState(
    val selectedFilterCategory: FilterCategory? = null,
    val appliedTypeFilters: Set<FilterOption.TransactionFilter.Type> = emptySet(),
    val appliedDateFilter: FilterOption.Date? = null
)