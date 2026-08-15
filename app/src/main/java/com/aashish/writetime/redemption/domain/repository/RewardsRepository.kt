package com.aashish.writetime.redemption.domain.repository

import com.aashish.writetime.redemption.domain.model.Reward

interface RewardsRepository {
    suspend fun addRewards(rewardList: List<Reward>)
    suspend fun getAllRewards(): List<Reward>
}