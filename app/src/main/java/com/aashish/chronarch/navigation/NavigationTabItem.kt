package com.aashish.chronarch.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.ViewWeek
import com.aashish.chronarch.R


sealed class NavigationTabItem(val route: String, @StringRes val labelRes: Int, val icon: ImageVector) {
    object Home: NavigationTabItem(Screen.Home.route, R.string.home_tab, Icons.Default.Home)
    object WeekOverview: NavigationTabItem(Screen.WeekOverview.route, R.string.weekly_overview_tab, Icons.Default.ViewWeek)
    object Redemption: NavigationTabItem(Screen.RedemptionCorner.route,  R.string.redemption_tab, Icons.Default.Redeem)
    object History: NavigationTabItem(Screen.History.route, R.string.history_tab, Icons.Default.History)
}

val navigationTabItems = listOf(
    NavigationTabItem.Home, NavigationTabItem.WeekOverview, NavigationTabItem.Redemption, NavigationTabItem.History
)

