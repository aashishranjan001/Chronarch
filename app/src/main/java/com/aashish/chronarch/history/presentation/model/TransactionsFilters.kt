package com.aashish.chronarch.history.presentation.model

data class TransactionsFilters(
    val appliedTypeFilters: Set<filterOption.TransactionFilter.Type> = emptySet(),
    val appliedDateFilter: filterOption.Date? = null
)