package com.aashish.writetime.common.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.aashish.writetime.common.data.local.model.TimerSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerSessionDao {

    @Upsert
    suspend fun upsert(session: TimerSessionEntity)

    @Query("SELECT * FROM timer_session WHERE startedAt >= :startTimeEpoch AND startedAt < :endTimeEpoch")
    fun getSessions(startTimeEpoch: Long, endTimeEpoch: Long): Flow<List<TimerSessionEntity>>
}