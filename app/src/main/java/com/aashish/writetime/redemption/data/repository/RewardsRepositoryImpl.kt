package com.aashish.writetime.redemption.data.repository

import com.aashish.writetime.redemption.data.local.dao.RewardsDao
import com.aashish.writetime.redemption.data.local.toDomain
import com.aashish.writetime.redemption.data.local.toEntity
import com.aashish.writetime.redemption.domain.model.Reward
import com.aashish.writetime.redemption.domain.repository.RewardsRepository
import javax.inject.Inject

class RewardsRepositoryImpl @Inject constructor(
    private val rewardsDao: RewardsDao
): RewardsRepository {
    override suspend fun addRewards(rewardList: List<Reward>) {
        rewardsDao.insert(
            rewardList.map { it.toEntity() }
        )
    }

    override suspend fun getAllRewards(): List<Reward> {
        return rewardsDao.getAllRewards().map { it.toDomain() }
    }
}