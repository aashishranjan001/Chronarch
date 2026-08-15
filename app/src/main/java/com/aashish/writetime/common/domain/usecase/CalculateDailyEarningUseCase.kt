package com.aashish.writetime.common.domain.usecase

import com.aashish.writetime.common.domain.model.EarningOverview
import com.aashish.writetime.common.domain.model.FocusPointTransactionType
import com.aashish.writetime.common.domain.repository.TransactionsHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class CalculateDailyEarningUseCase @Inject constructor(
    private val transactionsHistoryRepository: TransactionsHistoryRepository
) {
    operator fun invoke(date: LocalDate): Flow<EarningOverview> {
        val startOfDayTimestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val nextDayStartTimestamp = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

        return transactionsHistoryRepository.getTransactions(startOfDayTimestamp, nextDayStartTimestamp).map { transaction ->
            EarningOverview(
                earnedFocusPoints = transaction.filter { it.transactionType == FocusPointTransactionType.EARN }
                    .sumOf { it.value },
                bonusFocusPoints = transaction.filter { it.transactionType == FocusPointTransactionType.BONUS }
                    .sumOf { it.value },
                redeemedFocusPoints = transaction.filter { it.transactionType == FocusPointTransactionType.REDEEM }
                    .sumOf { it.value }
            )
        }
    }
}