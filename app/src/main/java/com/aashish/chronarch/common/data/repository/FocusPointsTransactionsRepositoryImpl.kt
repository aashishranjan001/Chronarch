package com.aashish.chronarch.common.data.repository

import com.aashish.chronarch.common.data.local.dao.TransactionHistoryDao
import com.aashish.chronarch.common.data.local.mappers.toDomain
import com.aashish.chronarch.common.data.local.mappers.toEntity
import com.aashish.chronarch.common.domain.model.FocusPointTransaction
import com.aashish.chronarch.common.domain.repository.FocusPointsTransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class FocusPointsTransactionsRepositoryImpl @Inject constructor(
    private val transactionHistoryDao: TransactionHistoryDao
): FocusPointsTransactionsRepository {
    override suspend fun registerTransaction(transaction: FocusPointTransaction) {
        transactionHistoryDao.insert(transaction.toEntity())
    }

    override suspend fun registerTransactions(transaction: List<FocusPointTransaction>) {
        transactionHistoryDao.insert(transaction.map { it.toEntity() })
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

    override fun getAllTransactions(): Flow<List<FocusPointTransaction>> {
        return transactionHistoryDao.getAllTransactions().map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getAvailableBalance(): Flow<Int> {
        return transactionHistoryDao.getAvailableBalance()
    }
}