package com.aashish.chronarch.redemption.domain.usecase

import com.aashish.chronarch.redemption.domain.model.Reward
import com.aashish.chronarch.redemption.domain.repository.RewardsRepository
import javax.inject.Inject

class AddRewardsUseCase @Inject constructor(
    private val rewardsRepository: RewardsRepository
) {
    suspend operator fun invoke(rewards: List<Reward>) {
        rewardsRepository.addRewards(rewards)
    }
}