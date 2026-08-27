package com.aashish.chronarch.redemption.presentation.rewards_setup

sealed class SetupRewardsEvent {
    data object AddActionRewardClick: SetupRewardsEvent()
    data class DeleteActionRewardClick(val reward: RewardSetupItem): SetupRewardsEvent()
    data object AbortSetupClick: SetupRewardsEvent()
    data object SaveChangesClick: SetupRewardsEvent()

    data class RewardItemClick(val reward: RewardSetupItem): SetupRewardsEvent()

    data object BackPressed: SetupRewardsEvent()

    data class CurrentRewardNameChanged(val name: String): SetupRewardsEvent()
    data class CurrentRewardCostChanged(val cost: String): SetupRewardsEvent()

    data class OverlayConfirmClicked(val overlayType: RewardsSetupOverlayType): SetupRewardsEvent()
    data class OverlayDismissed(val overlayType: RewardsSetupOverlayType): SetupRewardsEvent()
}

sealed class SetupRewardsUiEffect {
    data object RewardCostValueErrorSnackbar: SetupRewardsUiEffect()
    data object InvalidRewardInputError: SetupRewardsUiEffect()
    data object RewardDeletedSnackbar: SetupRewardsUiEffect()
    data object Finish: SetupRewardsUiEffect()
}