package com.aashish.chronarch.history.presentation.model

data class TransactionsFilters(
    val appliedTypeFilters: Set<FilterOption.TransactionFilter.Type> = emptySet(),
    val appliedDateFilter: FilterOption.Date? = null
)