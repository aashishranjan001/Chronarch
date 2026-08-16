package com.aashish.writetime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aashish.writetime.navigation.Screen
import com.aashish.writetime.navigation.WriteTimeBottomBar
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.home.presentation.HomeScreenRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WriteTimeTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        WriteTimeBottomBar(navController)
                    },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreenRoute()
                        }
                        composable(Screen.WeekOverview.route) {

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