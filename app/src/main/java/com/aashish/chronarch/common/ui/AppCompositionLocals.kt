package com.aashish.chronarch.common.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aashish.chronarch.common.ui.theme.RewardColors

val LocalSpacing = staticCompositionLocalOf<Spacing> {
    Spacing()
}

val LocalRewardColors = staticCompositionLocalOf<RewardColors> {
    error("RewardColors not provided")
}

data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 64.dp
)