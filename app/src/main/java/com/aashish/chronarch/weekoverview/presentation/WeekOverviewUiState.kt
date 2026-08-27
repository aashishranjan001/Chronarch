package com.aashish.chronarch.weekoverview.presentation

import com.aashish.chronarch.common.domain.model.EarningOverview
import com.aashish.chronarch.weekoverview.domain.model.WeekSessionOverview
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

data class WeekOverviewUiState(
    val selectedFocusPointsViewFilter: FocusPointsViewFilter = FocusPointsViewFilter.ALL,
    val dailyPointsMap: Map<DayOfWeek, EarningOverview?> = emptyMap(),
    val totalTaskCreditPoints: Int = 0,
    val totalBonusEarningPoints: Int = 0,
    val totalRedemptionPoints: Int = 0,
    val weekSessionOverview: WeekSessionOverview? = null,
    val showFocusPointsFilterMenuExpanded: Boolean = false,
) {
    val netFocusPointsEarnings
        get() = totalTaskCreditPoints + totalBonusEarningPoints - totalRedemptionPoints

    fun toDailyTransactionTypeMap(filter: FocusPointsViewFilter): Map<String, Int> {
        return dailyPointsMap.map { (dayOfWeek, earningOverview) ->
            val txnValue = when(filter) {
                FocusPointsViewFilter.ALL -> earningOverview?.netFocusPoints ?: 0
                FocusPointsViewFilter.TASK_CREDIT -> earningOverview?.taskCreditFocusPoints ?: 0
                FocusPointsViewFilter.BONUS -> earningOverview?.bonusFocusPoints ?: 0
                FocusPointsViewFilter.REDEEM -> earningOverview?.redeemedFocusPoints ?: 0
            }

            dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale.getDefault()
            ) to txnValue
        }.toMap()
    }
}