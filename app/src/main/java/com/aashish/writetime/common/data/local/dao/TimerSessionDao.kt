package com.aashish.writetime.common.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aashish.writetime.common.data.local.model.TimerSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerSessionDao {

    @Insert
    suspend fun insert(session: TimerSessionEntity)

    @Query("""
        UPDATE timer_session 
            SET completedAt = :endTime, streakProgressFraction = :streakProgressFraction 
        WHERE id = :sessionId
    """)
    suspend fun updateTimerCompletion(sessionId: Long, endTime: Long, streakProgressFraction: Double)

    @Query("SELECT * FROM timer_session WHERE startedAt >= :startTimeEpoch AND startedAt < :endTimeEpoch ORDER BY startedAt")
    fun getSessions(startTimeEpoch: Long, endTimeEpoch: Long): Flow<List<TimerSessionEntity>>

    @Query("SELECT * FROM timer_session WHERE completedAt IS NOT NULL ORDER BY startedAt DESC")
    fun getAllEndedSessions(): Flow<List<TimerSessionEntity>>

}