package com.aashish.chronarch.common.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aashish.chronarch.common.data.local.model.FocusPointsTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(focusPointsTransactionEntity: FocusPointsTransactionEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(focusPointsTransactionEntity: List<FocusPointsTransactionEntity>)

    @Query(
        """
        SELECT * FROM focus_points_transactions WHERE timestampMillis >= :startTimeEpoch AND timestampMillis < :endTimeEpoch 
    """
    )
    fun getTransactions(
        startTimeEpoch: Long,
        endTimeEpoch: Long
    ): Flow<List<FocusPointsTransactionEntity>>

    @Query("SELECT * FROM focus_points_transactions ORDER BY timestampMillis DESC")
    fun getAllTransactions(): Flow<List<FocusPointsTransactionEntity>>

    @Query(
        """
            SELECT COALESCE(
            SUM(
                CASE
                    WHEN transactionType = 'COMPLETION_CREDIT' THEN focusPoints
                    WHEN transactionType = 'BONUS_CREDIT' THEN focusPoints
                    WHEN transactionType = 'REDEMPTION_DEBIT' THEN -focusPoints
                    ELSE 0
                END
            ),
            0
        )
        FROM focus_points_transactions
        """
    )
    fun getAvailableBalance(): Flow<Int>
}