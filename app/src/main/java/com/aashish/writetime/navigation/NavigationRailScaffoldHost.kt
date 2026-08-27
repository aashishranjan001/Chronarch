package com.aashish.writetime.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
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
fun NavigationRailScaffoldHost(
    currentDestination: NavDestination?,
    navController: NavHostController,
    showNavigationTabs: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Row(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            if (showNavigationTabs) {
                WriteTimeNavigationRail(
                    navController = navController,
                    currentDestination = currentDestination,
                )
            }
            AppNavHost(
                navController = navController,
                startDestination = START_DESTINATION,
                snackbarHostState = snackbarHostState,
                modifier = Modifier.weight(1f)
            )
        }
    }
}