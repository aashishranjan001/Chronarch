package com.aashish.chronarch.redemption.domain.repository

import com.aashish.chronarch.redemption.domain.model.Reward
import kotlinx.coroutines.flow.Flow

interface RewardsRepository {
    suspend fun addRewards(rewardList: List<Reward>)
    fun getAllRewards(): Flow<List<Reward>>
}