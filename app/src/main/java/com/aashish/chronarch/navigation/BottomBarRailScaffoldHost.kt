package com.aashish.chronarch.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController

@Composable
fun BottomBarRailScaffoldHost(
    currentDestination: NavDestination?,
    navController: NavHostController,
    showNavigationTabs: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

    Scaffold(
        bottomBar = {
            if (showNavigationTabs) {
                ChronarchBottomBar(navController, currentDestination)
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        modifier = modifier
    ) { innerPadding ->

        AppNavHost(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            navController = navController,
            startDestination = START_DESTINATION,
            snackbarHostState = snackbarHostState
        )
    }
}