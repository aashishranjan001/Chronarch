package com.aashish.writetime.common.domain.repository

import com.aashish.writetime.common.domain.model.FocusPointTransaction
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface TransactionsHistoryRepository {
    suspend fun registerTransaction(transaction: FocusPointTransaction)
    fun getTransactions(startTime: Instant, endTime: Instant): Flow<List<FocusPointTransaction>>
}