package com.aashish.writetime.navigation

import androidx.annotation.StringRes

class AppBarState(
    @StringRes val titleRes: Int,
    val showBackButton: Boolean = false
    // add list of actions later when requirement comes for showing action options in app bar
)