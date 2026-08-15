package com.aashish.writetime.common.data.repository.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer_session")
data class TimerSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetDurationMillis: Long,
    val startedAt: Long,
    val completedAt: Long?,
    val streakProgressFraction: Double
)