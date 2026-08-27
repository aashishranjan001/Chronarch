package com.aashish.chronarch.weekoverview.presentation

sealed class WeekOverviewEvent {
    data class ApplyFocusPointsViewFilter(val filter: FocusPointsViewFilter): WeekOverviewEvent()
    data object FocusPointsViewFilterIconClicked: WeekOverviewEvent()
    data object DismissFocusPointsViewFilterMenu: WeekOverviewEvent()
}