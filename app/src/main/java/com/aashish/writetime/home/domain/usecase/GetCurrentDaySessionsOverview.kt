package com.aashish.writetime.home.domain.usecase

import com.aashish.writetime.common.domain.repository.TimerSessionRepository
import com.aashish.writetime.home.domain.model.ActiveSessionInfo
import com.aashish.writetime.home.domain.model.SessionsOverview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetCurrentDaySessionsOverview @Inject constructor(
    private val sessionRepository: TimerSessionRepository
) {
    operator fun invoke(): Flow<SessionsOverview> {
        val today = LocalDate.now()
        val startOfDayTimestamp: Instant = today.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val nextDayStartTimestamp: Instant = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

        return sessionRepository.getTimerSessions(startOfDayTimestamp, nextDayStartTimestamp).map { sessions ->
            SessionsOverview(
                successfulCompletionCount = sessions.filter { it.endTime != null && it.endTime >= it.idealCompletionTime}.size,
                cancelledCount = sessions.filter { it.endTime != null && it.idealCompletionTime > it.endTime }.size,
                totalStreaks = sessions.maxOfOrNull { it.streakProgressFraction }?.toInt() ?: 0,
                activeSession = sessions.find { it.endTime == null }?.let { session ->
                    ActiveSessionInfo(
                        id = session.id,
                        durationType = session.durationType,
                        idealCompletionTime = session.idealCompletionTime
                    )
                },
                currentStreakProgressionFraction = sessions.maxByOrNull { it.startTime }?.streakProgressFraction ?: 0.0
            )
        }
    }
}