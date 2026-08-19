package com.aashish.writetime.redemption.presentation.rewards_setup

data class SetupRewardsUiState(
    val addedRewardItems: List<RewardSetupItem> = emptyList(),
    val dialog: RewardsSetupDialogType? = null,
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

sealed class RewardsSetupDialogType {
    data object FinishSetupConfirmation : RewardsSetupDialogType()
    data object AbortSetupConfirmation : RewardsSetupDialogType()
    data object UpdateActionReward : RewardsSetupDialogType()
    data object AddActionReward : RewardsSetupDialogType()
    data class DeleteRewardConfirmation(val reward: RewardSetupItem) : RewardsSetupDialogType()
}