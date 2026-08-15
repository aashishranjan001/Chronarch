package com.aashish.writetime.common.data.repository

import com.aashish.writetime.common.data.local.dao.TransactionHistoryDao
import com.aashish.writetime.common.data.local.mappers.toDomain
import com.aashish.writetime.common.data.local.mappers.toEntity
import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.domain.repository.TransactionsHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

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
            startTime.toEpochMilli(),
            endTime.toEpochMilli()
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getAvailableBalance(): Flow<Int> {
        return transactionHistoryDao.getAvailableBalance()
    }
}