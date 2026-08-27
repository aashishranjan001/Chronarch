package com.aashish.chronarch.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun ChronarchBottomBar(
    navController: NavController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier) {

    BottomAppBar(
        modifier = modifier
    ) {
        navigationTabItems.forEach { tab ->
            AddTab(tab = tab,
                currentDestination = currentDestination,
                onClick = {
                    navController.navigateTopLevel(tab.route)
                }
            )
        }
    }
}

@Composable
fun RowScope.AddTab(
    tab: NavigationTabItem,
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