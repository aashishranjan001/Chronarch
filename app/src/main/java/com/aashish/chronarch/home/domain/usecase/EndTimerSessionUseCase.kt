package com.aashish.chronarch.home.domain.usecase

import com.aashish.chronarch.common.domain.model.DurationType
import com.aashish.chronarch.common.domain.model.FocusPointTransaction
import com.aashish.chronarch.common.domain.model.FocusPointTransactionType
import com.aashish.chronarch.common.domain.repository.TimerSessionRepository
import com.aashish.chronarch.common.domain.repository.FocusPointsTransactionsRepository
import java.time.Instant
import javax.inject.Inject
import kotlin.math.floor

class EndTimerSessionUseCase @Inject constructor(
    private val sessionRepository: TimerSessionRepository,
    private val focusPointsTransactionsRepository: FocusPointsTransactionsRepository
) {
    suspend operator fun invoke(
        sessionId: Long,
        idealCompletionTime: Instant,
        durationType: DurationType,
        streakProgressFraction: Double
    ) {
        val currentTime = Instant.now()

        val associatedFocusPoints = when (durationType) {
            DurationType.LongDuration -> 2
            DurationType.ShortDuration -> 1
        }

        val newStreakProgressFraction =
            if (currentTime >= idealCompletionTime) { // // successful completion
                streakProgressFraction + associatedFocusPoints.toDouble() / 4
            } else {
                floor(streakProgressFraction) // reset the streak level progress
            }

        sessionRepository.updateTimerSession(
            sessionId = sessionId,
            endTime = minOf(idealCompletionTime, currentTime),
            streakProgressFraction = newStreakProgressFraction
        )

        if (currentTime >= idealCompletionTime) { // successful completion
            // focus points credit transaction
            val focusPointTransactionList =
                buildList<FocusPointTransaction> { // insert of credit and bonus must be an atomic operation to prevent reactive reads side effects
                    add(
                        FocusPointTransaction(
                            id = 0,
                            value = associatedFocusPoints,
                            transactionType = FocusPointTransactionType.COMPLETION_CREDIT,
                            timestamp = currentTime,
                            message = "Earned for completing ${durationType.duration.inWholeMinutes} min session"
                        )
                    )
                    if (newStreakProgressFraction.toInt() == streakProgressFraction.toInt() + 1) { // new streak achieved
                        val streakLevel = newStreakProgressFraction.toInt()
                        add(
                            FocusPointTransaction(
                                id = 0,
                                value = streakLevel, // n points for completing nth streak
                                transactionType = FocusPointTransactionType.BONUS_CREDIT,
                                timestamp = currentTime,
                                message = "Bonus for reaching streak level: $streakLevel"
                            )
                        )
                    }
                }
            focusPointsTransactionsRepository.registerTransactions(focusPointTransactionList)
        }
    }
}