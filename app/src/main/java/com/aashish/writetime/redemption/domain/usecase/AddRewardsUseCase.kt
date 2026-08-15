package com.aashish.writetime.redemption.domain.usecase

import com.aashish.writetime.redemption.domain.model.Reward
import com.aashish.writetime.redemption.domain.repository.RewardsRepository
import javax.inject.Inject

class AddRewardsUseCase @Inject constructor(
    private val rewardsRepository: RewardsRepository
) {
    suspend operator fun invoke(rewards: List<Reward>) {
        rewardsRepository.addRewards(rewards)
    }
}