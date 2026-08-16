package com.aashish.writetime.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object WeekOverview: Screen("week_overview")
    object RedemptionLanding: Screen("redemption_landing")
    object RewardsSetup: Screen("rewards_setup")
    object RedemptionCorner: Screen("redemption_corner")
    object History: Screen("history")
}