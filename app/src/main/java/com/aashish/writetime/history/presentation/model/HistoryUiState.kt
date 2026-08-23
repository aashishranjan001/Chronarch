package com.aashish.writetime.history.presentation.model

data class HistoryUiState(
    val tabs: List<HistoryTab>,
    val selectedTab: HistoryTab,
    val allSessions: List<SessionHistoryItem> = emptyList(),
    val allTransactions: List<TransactionHistoryItem> = emptyList(),
    val filteredSessions: List<SessionHistoryItem> = emptyList(),
    val filteredTransactions: List<TransactionHistoryItem> = emptyList(),
    val appliedSessionsFilters: SessionsFilters = SessionsFilters(),
    val appliedTransactionsFilters: TransactionsFilters = TransactionsFilters(),
    val draftSessionsFilters: SessionsFilters? = null,
    val draftTransactionsFilters: TransactionsFilters? = null,
    val showDatePickerDialog: Boolean = false,
    val filterCategories: List<FilterCategoryItemUiState> = emptyList(),
    val filterOptions: List<FilterOptionItem> = emptyList()
) {
    val areSessionFilteredApplied
        get() = appliedSessionsFilters.appliedDateFilter != null
                || appliedSessionsFilters.appliedDurationTypeFilters.isNotEmpty()
                || appliedSessionsFilters.appliedCompletionStatusFilters.isNotEmpty()

    val areTransactionFiltersApplied
        get() = appliedTransactionsFilters.appliedDateFilter != null || appliedTransactionsFilters.appliedTypeFilters.isNotEmpty()

    val showFilterBottomSheet
        get() = draftSessionsFilters!= null || draftTransactionsFilters != null
}