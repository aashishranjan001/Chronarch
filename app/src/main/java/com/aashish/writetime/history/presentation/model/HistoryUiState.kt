package com.aashish.writetime.history.presentation.model

data class HistoryUiState(
    val tabs: List<HistoryTab>,
    val selectedTab: HistoryTab,
    val allSessions: List<SessionHistoryItem> = emptyList(),
    val allTransactions: List<TransactionHistoryItem> = emptyList(),
    val filteredSessions: List<SessionHistoryItem> = emptyList(),
    val filteredTransactions: List<TransactionHistoryItem> = emptyList(),
    val appliedSessionsFilters: SessionsFilterUiState = SessionsFilterUiState(),
    val appliedTransactionsFilters: TransactionsFilterUiState = TransactionsFilterUiState(),
    val draftSessionsFilters: SessionsFilterUiState? = null,
    val draftTransactionsFilters: TransactionsFilterUiState? = null,
    val showDatePickerDialog: Boolean = false,
    val filterCategories: List<FilterCategoryItemUiState> = emptyList(),
    val filterOptions: List<FilterOptionType> = emptyList()
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