package com.aashish.writetime.history.presentation.model

import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.domain.model.FocusPointTransactionType
import java.time.Instant

data class TransactionHistoryItem(
    val id: Long,
    val value: Int,
    val transactionType: FocusPointTransactionType,
    val timestamp: Instant,
    val message: String
) {
    fun toTransactionTypeFilterLabel(): FilterOption.TransactionFilter.Type {
        return when(transactionType) {
            FocusPointTransactionType.COMPLETION_CREDIT -> FilterOption.TransactionFilter.Type.TaskCredit
            FocusPointTransactionType.BONUS_CREDIT -> FilterOption.TransactionFilter.Type.Bonus
            FocusPointTransactionType.REDEMPTION_DEBIT -> FilterOption.TransactionFilter.Type.Redemption
        }
    }
}

fun FocusPointTransaction.toUiModel() = TransactionHistoryItem(
    id = id,
    value = value,
    transactionType = transactionType,
    timestamp = timestamp,
    message = message
)