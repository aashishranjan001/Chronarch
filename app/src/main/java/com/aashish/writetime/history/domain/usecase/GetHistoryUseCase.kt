package com.aashish.writetime.history.domain.usecase

import com.aashish.writetime.common.domain.repository.TimerSessionRepository
import com.aashish.writetime.common.domain.repository.FocusPointsTransactionsRepository
import com.aashish.writetime.history.domain.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val focusPointsTransactionsRepository: FocusPointsTransactionsRepository,
    private val sessionRepository: TimerSessionRepository
) {
    operator fun invoke(): Flow<History> {
        val startTime = Instant.ofEpochMilli(0L)
        val endTime = Instant.now()

        return combine(
            focusPointsTransactionsRepository.getTransactions(startTime, endTime),
            sessionRepository.getTimerSessions(startTime, endTime)
        ) { transactionList, sessionList ->
            History(
                transactions = transactionList,
                sessions = sessionList
            )
        }
    }
}