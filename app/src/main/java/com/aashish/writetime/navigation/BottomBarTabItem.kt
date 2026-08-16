package com.aashish.writetime.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.ViewWeek
import com.aashish.writetime.R


sealed class BottomBarTabItem(val route: String, @StringRes val labelRes: Int, val icon: ImageVector) {
    object Home: BottomBarTabItem(Screen.Home.route, R.string.home_tab, Icons.Default.Home)
    object WeekOverview: BottomBarTabItem(Screen.WeekOverview.route, R.string.weekly_overview_tab, Icons.Default.ViewWeek)
    object Redemption: BottomBarTabItem(Screen.RedemptionLanding.route,  R.string.redemption_tab, Icons.Default.Redeem)
    object History: BottomBarTabItem(Screen.History.route, R.string.history_tab, Icons.Default.History)
}

