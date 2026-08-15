package com.aashish.writetime.common.data.repository

import com.aashish.writetime.common.data.local.dao.TimerSessionDao
import com.aashish.writetime.common.data.local.mappers.toDomain
import com.aashish.writetime.common.data.local.mappers.toEntity
import com.aashish.writetime.common.domain.model.TimerSession
import com.aashish.writetime.common.domain.repository.TimerSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class TimerSessionRepositoryImpl @Inject constructor(
    private val timerSessionDao: TimerSessionDao
): TimerSessionRepository {
    override suspend fun addOrUpdateTimerSession(session: TimerSession) {
        timerSessionDao.upsert(
            session.toEntity()
        )
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
}