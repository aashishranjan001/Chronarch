package com.aashish.writetime.redemption.domain.usecase

import com.aashish.writetime.common.domain.repository.TransactionsHistoryRepository
import com.aashish.writetime.redemption.domain.model.RedemptionCornerOverview
import com.aashish.writetime.redemption.domain.repository.RewardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRedemptionCornerOverview @Inject constructor(
    private val rewardsRepository: RewardsRepository,
    private val transactionsHistoryRepository: TransactionsHistoryRepository
) {
    suspend operator fun invoke(): Flow<RedemptionCornerOverview> {
        return transactionsHistoryRepository.getAvailableBalance().map { availableBalance ->
            RedemptionCornerOverview(
                availableBalance = availableBalance,
                rewards = rewardsRepository.getAllRewards()
            )
        }
    }
}