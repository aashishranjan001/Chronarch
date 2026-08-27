package com.aashish.chronarch.redemption.domain.usecase

import com.aashish.chronarch.common.domain.repository.FocusPointsTransactionsRepository
import com.aashish.chronarch.redemption.domain.model.RedeemableReward
import com.aashish.chronarch.redemption.domain.model.RedemptionCornerOverview
import com.aashish.chronarch.redemption.domain.repository.RewardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetRedemptionCornerOverviewUseCase @Inject constructor(
    private val rewardsRepository: RewardsRepository,
    private val focusPointsTransactionsRepository: FocusPointsTransactionsRepository
) {
    operator fun invoke(): Flow<RedemptionCornerOverview> {
        return combine(
            focusPointsTransactionsRepository.getAvailableBalance(),
            rewardsRepository.getAllRewards()
        ) { availableBalance, allRewards ->
            RedemptionCornerOverview(
                availableBalance = availableBalance,
                rewards = allRewards.map { reward ->
                    RedeemableReward(
                        id = reward.id,
                        name = reward.name,
                        cost = reward.cost,
                        isRedeemable = reward.cost <= availableBalance
                    )
                }
            )
        }
    }
}