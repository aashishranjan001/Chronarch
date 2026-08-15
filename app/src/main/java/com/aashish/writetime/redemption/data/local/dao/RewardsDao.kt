package com.aashish.writetime.redemption.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aashish.writetime.redemption.data.local.model.RewardEntity

@Dao
interface RewardsDao {

    @Insert
    suspend fun insert(rewardList: List<RewardEntity>)

    @Query("""
        SELECT * FROM rewards
    """)
    suspend fun getRewardActions(): List<RewardEntity>
}