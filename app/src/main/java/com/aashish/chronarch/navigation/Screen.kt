package com.aashish.chronarch.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Screen(val route: String): Parcelable {
    @Parcelize
    data object Home: Screen("home")

    @Parcelize
    data object WeekOverview: Screen("week_overview")

    @Parcelize
    data object RedemptionCorner: Screen("redemption_corner")

    @Parcelize
    object RewardsSetup: Screen("rewards_setup")

    @Parcelize
    object History: Screen("history")
}

val START_DESTINATION = Screen.Home