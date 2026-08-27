package com.aashish.chronarch.history.presentation.model

sealed class FilterCategory {
    sealed class SessionFilter: FilterCategory() {
        data object DurationType: SessionFilter()
        data object CompletionStatus: SessionFilter()
    }

    sealed class TransactionFilter: FilterCategory() {
        data object Type: TransactionFilter()
    }
    data object Date: FilterCategory()
}