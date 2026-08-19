package com.aashish.writetime.redemption.presentation.rewards_setup

sealed class SetupRewardsEvent {
    data object AddActionRewardClick: SetupRewardsEvent()
    data class DeleteActionRewardClick(val reward: RewardSetupItem): SetupRewardsEvent()
    data object AbortSetupClick: SetupRewardsEvent()
    data object SaveChangesClick: SetupRewardsEvent()

    data class RewardItemClick(val reward: RewardSetupItem): SetupRewardsEvent()

    data object BackPressed: SetupRewardsEvent()

    data class CurrentRewardNameChanged(val name: String): SetupRewardsEvent()
    data class CurrentRewardCostChanged(val cost: String): SetupRewardsEvent()

    data class DialogConfirmClicked(val dialogType: RewardsSetupDialogType): SetupRewardsEvent()
    data class DialogDismissed(val dialogType: RewardsSetupDialogType): SetupRewardsEvent()
}

sealed class SetupRewardsUiEffect {
    data class RewardAddedSnackbar(val isSuccess: Boolean): SetupRewardsUiEffect()
    data class RewardUpdatedSnackbar(val isSuccess: Boolean): SetupRewardsUiEffect()
    data object RewardDeletedSnackbar: SetupRewardsUiEffect()
    data object Finish: SetupRewardsUiEffect()
}