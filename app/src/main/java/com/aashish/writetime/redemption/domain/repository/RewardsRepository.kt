package com.aashish.writetime.redemption.domain.repository

import com.aashish.writetime.redemption.domain.model.Reward
import kotlinx.coroutines.flow.Flow

interface RewardsRepository {
    suspend fun addRewards(rewardList: List<Reward>)
    fun getAllRewards(): Flow<List<Reward>>
}