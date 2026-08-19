package com.aashish.writetime.redemption.domain.usecase

import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.domain.model.FocusPointTransactionType
import com.aashish.writetime.common.domain.repository.FocusPointsTransactionsRepository
import com.aashish.writetime.redemption.domain.model.Reward
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
                message = "Claimed for reward activity: $rewardName"
            )
        )
    }

}