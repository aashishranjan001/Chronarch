package com.aashish.writetime.redemption.domain.usecase

import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.domain.model.FocusPointTransactionType
import com.aashish.writetime.common.domain.repository.TransactionsHistoryRepository
import com.aashish.writetime.redemption.domain.model.Reward
import java.time.Instant
import javax.inject.Inject

class RedeemRewardUseCase @Inject constructor(
    private val transactionsHistoryRepository: TransactionsHistoryRepository
) {
    suspend operator fun invoke(reward: Reward) {
        transactionsHistoryRepository.registerTransaction(
            FocusPointTransaction(
                id = 0,
                value = reward.cost,
                transactionType = FocusPointTransactionType.REDEEM,
                timestamp = Instant.now(),
                message = "Claimed for reward activity: ${reward.name}"
            )
        )
    }

}