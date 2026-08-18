package com.aashish.writetime.navigation

import androidx.navigation.NavDestination
import com.aashish.writetime.R

fun NavDestination.getAppBarSpec(): AppBarState? { // use navBackStackEntry if arguments from resolved route is to be used to construct app bar title
    return when(route) {
        Screen.RewardsSetup.route -> AppBarState(
            titleRes = R.string.title_rewards_setup,
            showBackButton = true
        )
        else -> null
    }
}