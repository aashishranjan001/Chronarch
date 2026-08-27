package com.aashish.chronarch.redemption.presentation.rewards_setup

data class SetupRewardsUiState(
    val addedRewardItems: List<RewardSetupItem> = emptyList(),
    val overlay: RewardsSetupOverlayType? = null,
    val currentEditingReward: EditModeRewardItem? = null
)

data class RewardSetupItem(
    val id: String,
    val name: String,
    val cost: Int,
)
data class EditModeRewardItem(
    val id: String,
    val name: String,
    val cost: String
)

sealed class RewardsSetupOverlayType {
    data object FinishSetupDialog : RewardsSetupOverlayType()
    data object AbortSetupDialog : RewardsSetupOverlayType()
    data object UpdateRewardBottomSheet : RewardsSetupOverlayType()
    data object AddRewardBottomSheet : RewardsSetupOverlayType()
    data class DeleteRewardDialog(val reward: RewardSetupItem) : RewardsSetupOverlayType()
}