package com.aashish.writetime.home.domain.usecase

import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.domain.model.FocusPointTransactionType
import com.aashish.writetime.common.domain.model.TimerSession
import com.aashish.writetime.common.domain.repository.TimerSessionRepository
import com.aashish.writetime.common.domain.repository.TransactionsHistoryRepository
import java.time.Instant
import javax.inject.Inject
import kotlin.math.floor

class UpdateTimerSessionCompletion @Inject constructor(
    private val sessionRepository: TimerSessionRepository,
    private val transactionsHistoryRepository: TransactionsHistoryRepository
) {

    suspend operator fun invoke(session: TimerSession, completedAt: Instant) {
        val previousStreakProgress = session.streakProgressFraction
        val newStreakProgress : Double

        if (completedAt == session.idealCompletionTime) {
            // successful completion
            newStreakProgress = previousStreakProgress + session.associatedFocusPoints.toDouble() / 4

            // focus points credit transaction
            transactionsHistoryRepository.registerTransaction(
                FocusPointTransaction(
                    id = 0,
                    value = session.associatedFocusPoints,
                    transactionType = FocusPointTransactionType.EARN,
                    timestamp = completedAt,
                    message = "Earned for successful completion of ${session.durationType.duration.inWholeMinutes} min session"
                )
            )

            if (newStreakProgress.toInt() == previousStreakProgress.toInt() + 1) { // new streak achieved
                val streakLevel = newStreakProgress.toInt()
                transactionsHistoryRepository.registerTransaction(
                    FocusPointTransaction(
                        id = 0,
                        value = streakLevel, // n points for completing nth streak
                        transactionType = FocusPointTransactionType.BONUS,
                        timestamp = completedAt,
                        message = "Bonus for reaching $streakLevel level"
                    )
                )
            }
        } else {
            newStreakProgress = floor(previousStreakProgress) // reset the streak level progress
        }

        sessionRepository.addOrUpdateTimerSession(
            session.copy(
                endTime = completedAt,
                streakProgressFraction = newStreakProgress
            )
        )
    }
}