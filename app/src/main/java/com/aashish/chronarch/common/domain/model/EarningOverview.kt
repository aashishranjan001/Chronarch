package com.aashish.chronarch.common.domain.model

data class EarningOverview(
    val taskCreditFocusPoints: Int,
    val bonusFocusPoints: Int,
    val redeemedFocusPoints: Int,
) {
    val netFocusPoints: Int
        get() = taskCreditFocusPoints + bonusFocusPoints - redeemedFocusPoints
}