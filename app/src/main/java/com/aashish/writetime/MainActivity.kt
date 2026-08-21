package com.aashish.writetime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aashish.writetime.navigation.Screen
import com.aashish.writetime.navigation.WriteTimeBottomBar
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.HistoryScreenRoute
import com.aashish.writetime.home.presentation.HomeScreenRoute
import com.aashish.writetime.navigation.bottomNavTabs
import com.aashish.writetime.redemption.presentation.redemption_corner.RedemptionCornerScreenRoute
import com.aashish.writetime.redemption.presentation.rewards_setup.SetupRewardsScreenRoute
import com.aashish.writetime.weekoverview.presentation.WeekOverviewScreenRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WriteTimeTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination

                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    bottomBar = {
                        if (bottomNavTabs.any { it.route == currentDestination?.route}) {
                            WriteTimeBottomBar(navController, currentDestination)
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->

                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Home.route
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
            }
        }
    }
}