package com.aashish.chronarch.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aashish.chronarch.history.presentation.HistoryScreenRoute
import com.aashish.chronarch.home.presentation.HomeScreenRoute
import com.aashish.chronarch.redemption.presentation.redemption_corner.RedemptionCornerScreenRoute
import com.aashish.chronarch.redemption.presentation.rewards_setup.SetupRewardsScreenRoute
import com.aashish.chronarch.weekoverview.presentation.WeekOverviewScreenRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Screen,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Screen.Home.route) {
            HomeScreenRoute(
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.WeekOverview.route) {
            WeekOverviewScreenRoute()
        }
        composable(Screen.RedemptionCorner.route) {
            RedemptionCornerScreenRoute(
                snackbarHostState = snackbarHostState,
                onLaunchRewardsSetup = {
                    navController.navigate(Screen.RewardsSetup.route)
                }
            )
        }
        composable(Screen.RewardsSetup.route) {
            SetupRewardsScreenRoute(
                snackbarHostState = snackbarHostState,
                onFinish = { navController.popBackStack() }
            )
        }
        composable(Screen.History.route) {
            HistoryScreenRoute()
        }
    }
}