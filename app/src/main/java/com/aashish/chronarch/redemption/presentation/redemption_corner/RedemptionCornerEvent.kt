package com.aashish.chronarch.redemption.presentation.redemption_corner

import com.aashish.chronarch.redemption.domain.model.RedeemableReward

sealed class RedemptionCornerEvent {
    object SetupRewardActions: RedemptionCornerEvent()
    data class RewardActionClick(val reward: RedeemableReward): RedemptionCornerEvent()
    data object DismissRewardsRedeemDialog: RedemptionCornerEvent()
    data class RewardsRedeemDialogOptionClick(val confirmed: Boolean): RedemptionCornerEvent()
    data object RetryClick: RedemptionCornerEvent()
}
