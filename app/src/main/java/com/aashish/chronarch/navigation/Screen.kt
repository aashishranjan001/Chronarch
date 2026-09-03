package com.aashish.chronarch.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Screen(val route: String): Parcelable {
    data object Home: Screen("home")

    data object WeekOverview: Screen("week_overview")

    data object RedemptionCorner: Screen("redemption_corner")

    object RewardsSetup: Screen("rewards_setup")

    object History: Screen("history")
}

val START_DESTINATION = Screen.Home