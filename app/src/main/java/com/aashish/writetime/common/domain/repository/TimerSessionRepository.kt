package com.aashish.writetime.common.domain.repository

import com.aashish.writetime.common.domain.model.TimerSession
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalTime

interface TimerSessionRepository {

    suspend fun addNewTimerSession(session: TimerSession)

    suspend fun updateSessionCompletion(sessionId: Long, completionTimestamp: Instant, streakProgressFraction: Double)

    fun getTimerSessions(startTime: Instant, endTime: Instant): Flow<List<TimerSession>>
}