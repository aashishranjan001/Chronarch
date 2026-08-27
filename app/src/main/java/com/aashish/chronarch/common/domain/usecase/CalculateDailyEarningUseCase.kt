package com.aashish.chronarch.common.domain.usecase

import com.aashish.chronarch.common.domain.model.EarningOverview
import com.aashish.chronarch.common.domain.model.FocusPointTransactionType
import com.aashish.chronarch.common.domain.repository.FocusPointsTransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class CalculateDailyEarningUseCase @Inject constructor(
    private val focusPointsTransactionsRepository: FocusPointsTransactionsRepository
) {
    operator fun invoke(date: LocalDate): Flow<EarningOverview> {
        val startOfDayTimestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val nextDayStartTimestamp = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

        return focusPointsTransactionsRepository.getTransactions(startOfDayTimestamp, nextDayStartTimestamp).map { transaction ->
            EarningOverview(
                taskCreditFocusPoints = transaction.filter { it.transactionType == FocusPointTransactionType.COMPLETION_CREDIT }
                    .sumOf { it.value },
                bonusFocusPoints = transaction.filter { it.transactionType == FocusPointTransactionType.BONUS_CREDIT }
                    .sumOf { it.value },
                redeemedFocusPoints = transaction.filter { it.transactionType == FocusPointTransactionType.REDEMPTION_DEBIT }
                    .sumOf { it.value }
            )
        }
    }
}