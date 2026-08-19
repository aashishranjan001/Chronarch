package com.aashish.writetime.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object WeekOverview: Screen("week_overview")
    object RedemptionCorner: Screen("redemption_corner")
    object RewardsSetup: Screen("rewards_setup")
    object History: Screen("history")
}