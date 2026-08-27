package com.aashish.chronarch.redemption.domain.usecase

import com.aashish.chronarch.common.domain.model.FocusPointTransaction
import com.aashish.chronarch.common.domain.model.FocusPointTransactionType
import com.aashish.chronarch.common.domain.repository.FocusPointsTransactionsRepository
import com.aashish.chronarch.redemption.domain.model.Reward
import java.time.Instant
import javax.inject.Inject

class RedeemRewardUseCase @Inject constructor(
    private val focusPointsTransactionsRepository: FocusPointsTransactionsRepository
) {
    suspend operator fun invoke(rewardCost: Int, rewardName: String) {
        focusPointsTransactionsRepository.registerTransaction(
            FocusPointTransaction(
                id = 0,
                value = rewardCost,
                transactionType = FocusPointTransactionType.REDEMPTION_DEBIT,
                timestamp = Instant.now(),
                message = "Claimed for reward: $rewardName"
            )
        )
    }

}