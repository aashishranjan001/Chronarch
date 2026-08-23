package com.aashish.writetime.history.presentation.model

data class TransactionsFilters(
    val appliedTypeFilters: Set<FilterOption.TransactionFilter.Type> = emptySet(),
    val appliedDateFilter: FilterOption.Date? = null
)