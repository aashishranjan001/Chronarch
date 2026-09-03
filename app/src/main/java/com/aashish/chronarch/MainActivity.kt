package com.aashish.chronarch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.content.IntentCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.navigation.NavigationRailScaffoldHost
import com.aashish.chronarch.navigation.BottomBarRailScaffoldHost
import com.aashish.chronarch.navigation.Screen
import com.aashish.chronarch.navigation.navigateTopLevel
import com.aashish.chronarch.navigation.navigationTabItems
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val pendingDestination = MutableStateFlow<Screen?>(null)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)

        setContent {
            ChronarchTheme {
                val navController = rememberNavController()
                LaunchedEffect(Unit) {
                    pendingDestination.collect { targetDestination ->
                        targetDestination?.let {
                            if (navigationTabItems.any { it.route == targetDestination.route }) {
                                navController.navigateTopLevel(targetDestination.route)
                            } else {
                                navController.navigate(targetDestination.route) {
                                    launchSingleTop = true
                                }
                            }
                            pendingDestination.value = null // reset to avoid reuse on configuration change
                        }
                    }
                }

                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination
                val snackbarHostState = remember { SnackbarHostState() }
                val showNavigationTabs = navigationTabItems.any { it.route == currentDestination?.route}

                val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass

                // show nav rail iff (height < minHeight && width >= compact) OR width > medium
                val useNavigationRail = (
                        !windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
                                &&
                                windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val destination = IntentCompat.getParcelableExtra(intent, INTENT_EXTRA_DESTINATION, Screen::class.java)
        if (destination != null) {
            pendingDestination.update { destination }
            intent.removeExtra(INTENT_EXTRA_DESTINATION) // Prevent retriggering on configuration change
        }
    }

    companion object {

        const val INTENT_EXTRA_DESTINATION = "destination"
        fun getIntent(context: Context, destination: Screen): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(INTENT_EXTRA_DESTINATION, destination)
            }
        }
    }
}