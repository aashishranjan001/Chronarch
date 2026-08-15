package com.aashish.writetime.common.data.repository

import com.aashish.writetime.common.data.local.dao.TransactionHistoryDao
import com.aashish.writetime.common.data.local.mappers.toDomain
import com.aashish.writetime.common.data.local.mappers.toEntity
import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.domain.repository.TransactionsHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Instant

class TransactionsHistoryRepositoryImpl @Inject constructor(
    private val transactionHistoryDao: TransactionHistoryDao
): TransactionsHistoryRepository {
    override suspend fun registerTransaction(transaction: FocusPointTransaction) {
        transactionHistoryDao.insert(transaction.toEntity())
    }

    override fun getTransactions(
        startTime: Instant,
        endTime: Instant
    ): Flow<List<FocusPointTransaction>> {
        return transactionHistoryDao.getTransactions(
            startTime.toEpochMilliseconds(),
            endTime.toEpochMilliseconds()
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }
}