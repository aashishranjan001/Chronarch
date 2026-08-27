package com.aashish.chronarch.common.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aashish.chronarch.common.domain.model.FocusPointTransactionType

@Entity(tableName = "focus_points_transactions")
data class FocusPointsTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionType: FocusPointTransactionType,
    val focusPoints: Int,
    val timestampMillis: Long,
    val message: String
)