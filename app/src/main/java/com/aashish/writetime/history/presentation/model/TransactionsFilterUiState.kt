package com.aashish.writetime.history.presentation.model

data class TransactionsFilterUiState(
    val selectedFilterCategory: FilterCategory.TransactionFilter? = null,
    val appliedTypeFilters: Set<FilterOption.TransactionFilter.Type> = emptySet(),
    val appliedDateFilter: Pair<Long, Long>? = null
)