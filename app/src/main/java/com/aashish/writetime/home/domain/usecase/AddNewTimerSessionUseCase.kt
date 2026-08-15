package com.aashish.writetime.home.domain.usecase

import com.aashish.writetime.common.domain.model.DurationType
import com.aashish.writetime.common.domain.model.TimerSession
import com.aashish.writetime.common.domain.repository.TimerSessionRepository
import java.time.Instant
import javax.inject.Inject

class AddNewTimerSessionUseCase @Inject constructor(
    private val sessionRepository: TimerSessionRepository
) {

    suspend operator fun invoke(durationType: DurationType, runningStreakProgressFraction: Double) {
        sessionRepository.addOrUpdateTimerSession(
            TimerSession(
                id = 0,
                startTime = Instant.now(),
                endTime = null,
                durationType = durationType,
                streakProgressFraction = runningStreakProgressFraction
            )
        )
    }
}