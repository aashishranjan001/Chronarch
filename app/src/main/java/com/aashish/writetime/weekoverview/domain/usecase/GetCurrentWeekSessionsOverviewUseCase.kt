package com.aashish.writetime.weekoverview.domain.usecase

import com.aashish.writetime.common.domain.model.DurationType
import com.aashish.writetime.common.domain.repository.TimerSessionRepository
import com.aashish.writetime.weekoverview.domain.model.WeekSessionOverview
import com.aashish.writetime.weekoverview.domain.model.SessionCompletionCountOverview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetCurrentWeekSessionsOverviewUseCase @Inject constructor(
    private val sessionRepository: TimerSessionRepository
) {
    suspend operator fun invoke(): Flow<WeekSessionOverview> {
        val today = LocalDate.now()
        val startOfWeek= today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

        val startOfWeekTimestamp: Instant = startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val nextDayStartTimestamp: Instant = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

        return sessionRepository.getTimerSessions(startOfWeekTimestamp, nextDayStartTimestamp).map { sessions ->
            var shortSessionsSuccessfulCompletionCount = 0
            var shortSessionsCancellationCount = 0
            var longSessionsSuccessfulCompletionCount = 0
            var longSessionsCancellationCount = 0

            sessions.filter { it.endTime != null }.forEach { session ->
                val isCancelledSession = session.endTime != null && session.endTime < session.idealCompletionTime

                when(session.durationType) {
                    DurationType.LongDuration -> {
                        if (isCancelledSession) longSessionsCancellationCount++
                        else longSessionsSuccessfulCompletionCount++
                    }
                    DurationType.ShortDuration -> {
                        if (isCancelledSession) shortSessionsCancellationCount++
                        else shortSessionsSuccessfulCompletionCount++
                    }
                }
            }

            WeekSessionOverview(
                shortSessionsOverview = SessionCompletionCountOverview(
                    successfulCompletionCount = shortSessionsSuccessfulCompletionCount,
                    cancelledCount = shortSessionsCancellationCount
                ),
                longSessionsOverview = SessionCompletionCountOverview(
                    successfulCompletionCount = longSessionsSuccessfulCompletionCount,
                    cancelledCount = longSessionsCancellationCount
                )
            )
        }
    }
}