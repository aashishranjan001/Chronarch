package com.aashish.chronarch.home.domain.usecase

import com.aashish.chronarch.common.domain.repository.TimerSessionRepository
import com.aashish.chronarch.home.domain.model.LatestTimerSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLatestTimerSessionUseCase @Inject constructor(
    private val sessionRepository: TimerSessionRepository
) {
    operator fun invoke(): Flow<LatestTimerSession?> {
        return sessionRepository.getLatestTimer().map { latestSession ->
            latestSession?.let {
                LatestTimerSession(
                    id = it.id,
                    durationType = it.durationType,
                    sessionEndTime = it.endTime,
                    idealEndTime = it.idealCompletionTime,
                    runningStreakProgressionFraction = it.streakProgressFraction
                )
            }
        }
    }
}