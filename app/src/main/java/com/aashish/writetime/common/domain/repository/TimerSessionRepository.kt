package com.aashish.writetime.common.domain.repository

import com.aashish.writetime.common.domain.model.TimerSession
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface TimerSessionRepository {

    suspend fun addOrUpdateTimerSession(session: TimerSession)

    fun getTimerSessions(startTime: Instant, endTime: Instant): Flow<List<TimerSession>>
}