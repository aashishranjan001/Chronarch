package com.aashish.writetime.redemption.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aashish.writetime.redemption.data.local.model.RewardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardsDao {

    @Insert
    suspend fun insert(rewardList: List<RewardEntity>)

    @Query("""
        SELECT * FROM rewards ORDER BY focusPointsCost
    """)
    fun getAllRewards(): Flow<List<RewardEntity>>
}