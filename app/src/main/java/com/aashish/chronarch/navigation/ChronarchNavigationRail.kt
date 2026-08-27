package com.aashish.chronarch.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.aashish.chronarch.common.ui.LocalSpacing

@Composable
fun ChronarchNavigationRail(
    navController: NavController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier) {

    val spacing = LocalSpacing.current
    NavigationRail (
        modifier = modifier,
        windowInsets = WindowInsets()
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = spacing.medium, alignment = Alignment.CenterVertically)
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
}

@Composable
fun ColumnScope.AddTab(
    tab: NavigationTabItem,
    onClick: () -> Unit,
    currentDestination: NavDestination?,
) {
    NavigationRailItem(
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
        colors = NavigationRailItemDefaults.colors().copy(
            unselectedIconColor = LocalContentColor.current.copy(alpha = 0.6f),
            unselectedTextColor = LocalContentColor.current.copy(alpha = 0.6f),
        )
    )
}