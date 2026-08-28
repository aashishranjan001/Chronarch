package com.aashish.chronarch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.navigation.NavigationRailScaffoldHost
import com.aashish.chronarch.navigation.BottomBarRailScaffoldHost
import com.aashish.chronarch.navigation.navigationTabItems
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChronarchTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination

                val snackbarHostState = remember { SnackbarHostState() }
                val showNavigationTabs = navigationTabItems.any { it.route == currentDestination?.route}

                val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass

                // show nav rail iff (height < minHeight && width >= compact) OR width > medium
                val useNavigationRail =
                    (
                            !windowSizeClass.isHeightAtLeastBreakpoint(
                                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
                            ) &&
                                    windowSizeClass.isWidthAtLeastBreakpoint(
                                        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
                                    )
                            ) ||
                            windowSizeClass.isWidthAtLeastBreakpoint(
                                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
                            )
                if (useNavigationRail) {
                    NavigationRailScaffoldHost(
                        currentDestination = currentDestination,
                        navController = navController,
                        showNavigationTabs = showNavigationTabs,
                        snackbarHostState = snackbarHostState
                    )
                } else {
                    BottomBarRailScaffoldHost(
                        currentDestination = currentDestination,
                        navController = navController,
                        showNavigationTabs = showNavigationTabs,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}