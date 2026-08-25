package com.aashish.writetime.history.domain.usecase

import com.aashish.writetime.common.domain.repository.TimerSessionRepository
import com.aashish.writetime.common.domain.repository.FocusPointsTransactionsRepository
import com.aashish.writetime.history.domain.model.EndedTimerSession
import com.aashish.writetime.history.domain.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Duration
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class GetHistoryUseCase @Inject constructor(
    private val focusPointsTransactionsRepository: FocusPointsTransactionsRepository,
    private val sessionRepository: TimerSessionRepository
) {
    operator fun invoke(): Flow<History> {
        return combine(
            focusPointsTransactionsRepository.getAllTransactions(),
            sessionRepository.getAllTimerSessions()
        ) { transactionList, sessionList ->
            History(
                transactions = transactionList,
                sessions = sessionList.mapNotNull { session ->
                    session.endTime?.let {
                        EndedTimerSession(
                            id = session.id,
                            startTime = session.startTime,
                            endTime = it,
                            durationType = session.durationType,
                            runDuration = Duration.between(session.startTime, it).seconds.seconds
                        )
                    }
                }
            )
        }
    }
}