package com.aashish.chronarch.weekoverview.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.weekoverview.presentation.components.FocusPointsTransactionsBreakdown
import com.aashish.chronarch.weekoverview.presentation.components.FocusPointsTransactionsGraphCard
import com.aashish.chronarch.weekoverview.presentation.components.SessionsStatsSection

@Composable
fun WeekOverviewScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: WeekOverviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeekOverviewScreen(uiState = uiState, onEvent = viewModel::onEvent, modifier = modifier)
}

@Composable
fun WeekOverviewScreen(
    uiState: WeekOverviewUiState,
    onEvent: (WeekOverviewEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    LazyColumn(
        contentPadding = PaddingValues(spacing.medium),
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = spacing.medium)
    ) {
        item {
            FocusPointsTransactionsGraphCard(
                dailyTransactionPointsMap = uiState.toDailyTransactionTypeMap(uiState.selectedFocusPointsViewFilter),
                showFilterMenuExpanded = uiState.showFocusPointsFilterMenuExpanded,
                onFilterMenuDismissed = { onEvent(WeekOverviewEvent.DismissFocusPointsViewFilterMenu) },
                onFilterMenuIconClick = { onEvent(WeekOverviewEvent.FocusPointsViewFilterIconClicked) },
                selectedMenuOption = uiState.selectedFocusPointsViewFilter,
                onFilterMenuOptionSelected = {
                    onEvent(
                        WeekOverviewEvent.ApplyFocusPointsViewFilter(it)
                    )
                },
            )
        }
        item {
            FocusPointsTransactionsBreakdown(
                taskCredits = uiState.totalTaskCreditPoints,
                bonusPoints = uiState.totalBonusEarningPoints,
                redemptionPoints = uiState.totalRedemptionPoints,
                totalFocusPoints = uiState.netFocusPointsEarnings
            )
        }
        item {
            SessionsStatsSection(
                shortSessionsSuccessCount = uiState.weekSessionOverview?.shortSessionsOverview?.successfulCompletionCount ?: 0,
                totalShortSessionsCount = uiState.weekSessionOverview?.shortSessionsOverview?.totalAttemptedSessionsCount ?: 0,
                longSessionsSuccessCount = uiState.weekSessionOverview?.longSessionsOverview?.successfulCompletionCount ?: 0,
                totalLongSessionsCount = uiState.weekSessionOverview?.longSessionsOverview?.totalAttemptedSessionsCount ?: 0
            )
        }
    }
}

@Preview
@Composable
private fun WeekOverviewScreenPreview() {
    ChronarchTheme {
        WeekOverviewScreen(
            WeekOverviewUiState(), {}
        )
    }
}
