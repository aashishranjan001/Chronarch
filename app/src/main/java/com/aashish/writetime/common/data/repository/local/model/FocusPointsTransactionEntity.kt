package com.aashish.writetime.common.data.repository.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aashish.writetime.common.domain.model.FocusPointTransactionType

@Entity(tableName = "focus_points_transactions")
data class FocusPointsTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: Long,
    val transactionType: FocusPointTransactionType,
    val focusPoints: Long,
    val timestamp: Long,
    val message: String
)