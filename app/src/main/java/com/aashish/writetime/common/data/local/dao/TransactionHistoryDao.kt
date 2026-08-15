package com.aashish.writetime.common.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aashish.writetime.common.data.local.model.FocusPointsTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(focusPointsTransactionEntity: FocusPointsTransactionEntity)

    @Query(
        """
        SELECT * FROM focus_points_transactions WHERE timestampMillis >= :startTimeEpoch AND timestampMillis < :endTimeEpoch 
    """
    )
    fun getTransactions(startTimeEpoch: Long, endTimeEpoch: Long): Flow<List<FocusPointsTransactionEntity>>
}