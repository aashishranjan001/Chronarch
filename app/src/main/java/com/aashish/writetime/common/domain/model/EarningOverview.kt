package com.aashish.writetime.common.domain.model

data class EarningOverview(
    val earnedFocusPoints: Int,
    val bonusFocusPoints: Int,
    val redeemedFocusPoints: Int,
) {
    val netFocusPoints: Int
        get() = earnedFocusPoints + bonusFocusPoints - redeemedFocusPoints
}