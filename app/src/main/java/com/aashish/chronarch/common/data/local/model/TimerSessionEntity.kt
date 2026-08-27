package com.aashish.chronarch.common.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer_session")
data class TimerSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetDurationMillis: Long,
    val startedAt: Long,
    val completedAt: Long? = null,
    val streakProgressFraction: Double = 0.0
)