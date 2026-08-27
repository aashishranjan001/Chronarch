package com.aashish.chronarch.history.domain.usecase

import com.aashish.chronarch.common.domain.repository.TimerSessionRepository
import com.aashish.chronarch.common.domain.repository.FocusPointsTransactionsRepository
import com.aashish.chronarch.history.domain.model.EndedTimerSession
import com.aashish.chronarch.history.domain.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Duration
import javax.inject.Inject
import kotlin.time.toKotlinDuration

class GetHistoryUseCase @Inject constructor(
    private val focusPointsTransactionsRepository: FocusPointsTransactionsRepository,
    private val sessionRepository: TimerSessionRepository
) {
    operator fun invoke(): Flow<History> {
        return combine(
            focusPointsTransactionsRepository.getAllTransactions(),
            sessionRepository.getAllEndedTimerSessions()
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
                            runDuration = minOf(
                                Duration.between(session.startTime, it).toKotlinDuration(),
                                session.durationType.duration
                            )
                        )
                    }
                }
            )
        }
    }
}