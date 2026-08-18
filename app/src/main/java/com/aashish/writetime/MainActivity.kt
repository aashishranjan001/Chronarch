package com.aashish.writetime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aashish.writetime.navigation.Screen
import com.aashish.writetime.navigation.WriteTimeBottomBar
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.home.presentation.HomeScreenRoute
import com.aashish.writetime.weekoverview.presentation.WeekOverviewScreenRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WriteTimeTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    bottomBar = {
                        WriteTimeBottomBar(navController)
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->

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
                        composable(Screen.RedemptionLanding.route) {

                        }
                        composable(Screen.RewardsSetup.route) {

                        }
                        composable(Screen.RedemptionCorner.route) {

                        }
                        composable(Screen.History.route) {

                        }
                    }
                }
            }
        }
    }
}