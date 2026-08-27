package com.aashish.chronarch.home.domain.usecase

import com.aashish.chronarch.common.domain.model.DurationType
import com.aashish.chronarch.common.domain.model.TimerSession
import com.aashish.chronarch.common.domain.repository.TimerSessionRepository
import java.time.Instant
import javax.inject.Inject

class AddNewTimerSessionUseCase @Inject constructor(
    private val sessionRepository: TimerSessionRepository
) {

    suspend operator fun invoke(durationType: DurationType, runningStreakProgressFraction: Double) {
        sessionRepository.addNewTimerSession(
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