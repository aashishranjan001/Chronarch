package com.aashish.writetime.redemption.presentation.redemption_corner

import com.aashish.writetime.redemption.domain.model.RedeemableReward
import com.aashish.writetime.redemption.domain.model.Reward

data class RedemptionCornerUiState(
    val rewardsList: List<RedeemableReward> = emptyList(),
    val availableFocusPointsBalance: Int = 0,
    val selectedRedeemableReward: RedeemableReward? = null
) {
    val showRewardsSetup
        get() = rewardsList.isEmpty()
}

sealed class RedemptionCornerUiEffect {
    data object LaunchRewardsSetup: RedemptionCornerUiEffect()
    data class ShowRewardsRedeemedMessage(val rewardName: String): RedemptionCornerUiEffect()
}