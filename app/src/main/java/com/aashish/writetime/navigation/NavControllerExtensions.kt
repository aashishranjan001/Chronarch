package com.aashish.writetime.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.navigateTopLevel(targetRoute: String) {
    navigate(targetRoute) {
        launchSingleTop = true
        restoreState = true

        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}

fun NavController.navigateToCrossTabDestination(targetRoute: String) {
    navigate(targetRoute) {
        launchSingleTop = true

        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}