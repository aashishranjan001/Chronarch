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
                successfulCompletionCount = sessions.filter { it.endTime != null && it.idealCompletionTime == it.endTime}.size,
                cancelledCount = sessions.filter { it.endTime != null && it.idealCompletionTime > it.endTime }.size,
                totalStreaks = countStreaks(sessions.map { it.streakProgressFraction }),
                activeSession = sessions.find { it.endTime == null }?.let { session ->
                    ActiveSessionInfo(
                        id = session.id,
                        durationType = session.durationType,
                        durationRemainingSeconds = Duration.between(
                            Instant.now(), session.idealCompletionTime
                        ).seconds,
                    )
                },
                currentStreakProgressionFraction = sessions.maxByOrNull { it.startTime }?.streakProgressFraction ?: 0.0
            )
        }
    }

    private fun countStreaks(streakProgressionList: List<Double>): Int {
        var streakCount = 0
        for (i in 1 until streakProgressionList.size) {
            val currentStreakProgression = streakProgressionList[i]
            val previousStreakProgression = streakProgressionList[i-1]

            if (currentStreakProgression.toInt() - previousStreakProgression.toInt() == 1) {
                streakCount++
            }
        }
        return streakCount
    }
}