package com.aashish.writetime.common.domain.model

data class EarningOverview(
    val completionFocusPoints: Int,
    val bonusFocusPoints: Int,
    val redeemedFocusPoints: Int,
) {

    val creditFocusPoints
        get() = completionFocusPoints + bonusFocusPoints

    val netFocusPoints: Int
        get() = completionFocusPoints + bonusFocusPoints - redeemedFocusPoints
}