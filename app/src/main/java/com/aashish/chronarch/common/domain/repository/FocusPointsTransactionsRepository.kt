package com.aashish.chronarch.common.domain.repository

import com.aashish.chronarch.common.domain.model.FocusPointTransaction
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface FocusPointsTransactionsRepository {
    suspend fun registerTransaction(transaction: FocusPointTransaction)
    suspend fun registerTransactions(transaction: List<FocusPointTransaction>)


    fun getTransactions(startTime: Instant, endTime: Instant): Flow<List<FocusPointTransaction>>
    fun getAllTransactions(): Flow<List<FocusPointTransaction>>
    fun getAvailableBalance(): Flow<Int>
}