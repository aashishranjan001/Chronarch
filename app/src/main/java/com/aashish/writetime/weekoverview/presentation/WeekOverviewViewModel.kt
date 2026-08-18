package com.aashish.writetime.weekoverview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.writetime.weekoverview.domain.usecase.GetCurrentWeekEarningsOverviewUseCase
import com.aashish.writetime.weekoverview.domain.usecase.GetCurrentWeekSessionsOverviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeekOverviewViewModel @Inject constructor(
    private val getCurrentWeekEarningsOverviewUseCase: GetCurrentWeekEarningsOverviewUseCase,
    private val getCurrentWeekSessionsOverviewUseCase: GetCurrentWeekSessionsOverviewUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(WeekOverviewUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeWeekEarnings()
        observeWeekSessionStats()
    }

    fun onEvent(event: WeekOverviewEvent) {
        when(event) {
            is WeekOverviewEvent.ApplyFocusPointsViewFilter -> {
                _uiState.update {
                    it.copy(
                        selectedFocusPointsViewFilter = event.filter,
                        showFocusPointsFilterMenuExpanded = false
                    )
                }
            }

            WeekOverviewEvent.FocusPointsViewFilterIconClicked -> {
                _uiState.update {
                    it.copy(
                        showFocusPointsFilterMenuExpanded = !it.showFocusPointsFilterMenuExpanded
                    )
                }
            }

            WeekOverviewEvent.DismissFocusPointsViewFilterMenu -> {
                _uiState.update {
                    it.copy(
                        showFocusPointsFilterMenuExpanded = false
                    )
                }
            }
        }
    }

    private fun observeWeekEarnings() {
        viewModelScope.launch {
            getCurrentWeekEarningsOverviewUseCase().collectLatest { earningOverview ->
                _uiState.update {
                    it.copy(
                        totalTaskCreditPoints = earningOverview.totalEarningOverview.taskCreditFocusPoints,
                        totalBonusEarningPoints = earningOverview.totalEarningOverview.bonusFocusPoints,
                        totalRedemptionPoints = earningOverview.totalEarningOverview.redeemedFocusPoints,
                        dailyPointsMap = earningOverview.dayWiseEarningOverview
                    )
                }
            }
        }
    }

    private fun observeWeekSessionStats() {
        viewModelScope.launch {
            getCurrentWeekSessionsOverviewUseCase().collectLatest { sessionOverview ->
                _uiState.update {
                    it.copy(
                        weekSessionOverview = sessionOverview
                    )
                }
            }
        }
    }
}