package com.aashish.chronarch.common.data.repository

import com.aashish.chronarch.common.data.local.dao.TimerSessionDao
import com.aashish.chronarch.common.data.local.mappers.toDomain
import com.aashish.chronarch.common.data.local.mappers.toEntity
import com.aashish.chronarch.common.domain.model.TimerSession
import com.aashish.chronarch.common.domain.repository.TimerSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class TimerSessionRepositoryImpl @Inject constructor(
    private val timerSessionDao: TimerSessionDao
): TimerSessionRepository {
    override suspend fun addNewTimerSession(session: TimerSession) {
        timerSessionDao.insert(
            session.toEntity()
        )
    }

    override suspend fun updateTimerSession(
        sessionId: Long,
        endTime: Instant,
        streakProgressFraction: Double
    ) {
        timerSessionDao.updateTimerCompletion(sessionId, endTime.toEpochMilli(), streakProgressFraction)
    }

    override fun getTimerSessions(
        startTime: Instant,
        endTime: Instant
    ): Flow<List<TimerSession>> {
        return timerSessionDao.getSessions(
            startTime.toEpochMilli(),
            endTime.toEpochMilli()
        ).map { sessionsList ->
            sessionsList.map { it.toDomain() }
        }
    }

    override fun getLatestTimer(): Flow<TimerSession?> {
        return timerSessionDao.getLatestTimer().map { it?.toDomain() }
    }

    override fun getAllEndedTimerSessions(): Flow<List<TimerSession>> {
        return timerSessionDao.getAllEndedSessions().map { sessionsList ->
            sessionsList.map { it.toDomain() }
        }
    }
}