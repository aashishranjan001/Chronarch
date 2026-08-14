package com.aashish.writetime.common.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun WriteTimeBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier) {

    val bottomNavTabs = listOf(
        BottomBarTabItem.Home, BottomBarTabItem.WeekOverview, BottomBarTabItem.Redemption, BottomBarTabItem.History
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        modifier = modifier
    ) {
        bottomNavTabs.forEach { tab ->
            AddItem(tab = tab,
                currentDestination = currentDestination,
                onClick = {
                    navController.navigateTopLevel(tab.route)
                }
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    tab: BottomBarTabItem,
    onClick: () -> Unit,
    currentDestination: NavDestination?,
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == tab.route
        } == true,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = tab.icon, contentDescription = stringResource(tab.labelRes)
            )
        },
        label = {
            Text(text = stringResource(tab.labelRes))
        },
        colors = NavigationBarItemDefaults.colors().copy(
            unselectedIconColor = LocalContentColor.current.copy(alpha = 0.6f),
            unselectedTextColor = LocalContentColor.current.copy(alpha = 0.6f),
        )
    )
}