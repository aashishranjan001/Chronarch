package com.aashish.chronarch.redemption.presentation.redemption_corner

import com.aashish.chronarch.redemption.domain.model.RedeemableReward

data class RedemptionCornerUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
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