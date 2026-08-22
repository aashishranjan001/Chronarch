package com.aashish.writetime.history.presentation.model

sealed class FilterCategory {
    sealed class SessionFilter: FilterCategory() {
        data object DurationType: SessionFilter()
        data object CompletionStatus: SessionFilter()
        data object Date: SessionFilter()

        companion object {
            val entries by lazy {
                listOf(DurationType, CompletionStatus, Date)
            }
        }
    }

    sealed class TransactionFilter: FilterCategory() {
        data object Type: TransactionFilter()
        data object Date: TransactionFilter()

        companion object {
            val entries by lazy {
                listOf(Type, Date)
            }
        }
    }
}