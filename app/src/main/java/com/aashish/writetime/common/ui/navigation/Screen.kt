package com.aashish.writetime.common.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object WeekOverview: Screen("week_overview")
    object RedemptionLanding: Screen("redemption_landing")
    object ActionRewardsSetup: Screen("action_rewards_setup")
    object RedemptionCorner: Screen("redemption_corner")
    object History: Screen("history")
}