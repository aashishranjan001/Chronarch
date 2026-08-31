package com.aashish.chronarch.common.domain.repository

import com.aashish.chronarch.common.domain.model.TimerSession
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface TimerSessionRepository {

    suspend fun addNewTimerSession(session: TimerSession)

    suspend fun updateTimerSession(sessionId: Long, endTime: Instant, streakProgressFraction: Double)

    fun getTimerSessions(startTime: Instant, endTime: Instant): Flow<List<TimerSession>>

    fun getLatestTimer(): Flow<TimerSession?>
    fun getAllEndedTimerSessions(): Flow<List<TimerSession>>
}