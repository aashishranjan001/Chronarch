package com.aashish.writetime.common.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aashish.writetime.common.data.repository.local.model.FocusPointsTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(focusPointsTransactionEntity: FocusPointsTransactionEntity)

    @Query("""
        SELECT * FROM focus_points_transactions WHERE timestamp >= :startTimeEpoch AND timestamp < :endTimeEpoch 
    """)
    fun getTransactions(startTimeEpoch: Long, endTimeEpoch: Long): Flow<List<FocusPointsTransactionEntity>>
}