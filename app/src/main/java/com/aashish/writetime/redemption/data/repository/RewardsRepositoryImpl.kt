package com.aashish.writetime.redemption.data.repository

import com.aashish.writetime.redemption.data.local.dao.RewardsDao
import com.aashish.writetime.redemption.data.local.toDomain
import com.aashish.writetime.redemption.data.local.toEntity
import com.aashish.writetime.redemption.domain.model.Reward
import com.aashish.writetime.redemption.domain.repository.RewardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RewardsRepositoryImpl @Inject constructor(
    private val rewardsDao: RewardsDao
): RewardsRepository {
    override suspend fun addRewards(rewardList: List<Reward>) {
        rewardsDao.insert(
            rewardList.map { it.toEntity() }
        )
    }

    override fun getAllRewards(): Flow<List<Reward>> {
        return rewardsDao.getAllRewards().map { rewardsList ->
            rewardsList.map { it.toDomain() }
        }
    }
}