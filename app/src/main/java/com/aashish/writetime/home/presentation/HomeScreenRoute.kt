package com.aashish.writetime.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.home.presentation.components.SessionsCard
import com.aashish.writetime.home.presentation.components.SingleValueTile
import com.aashish.writetime.home.presentation.components.ActiveTimerSection
import com.aashish.writetime.home.presentation.components.NewTimerSection
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )

}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(spacing.medium),
        verticalArrangement = Arrangement.spacedBy(
            space = spacing.medium,
            alignment = Alignment.CenterVertically
        )
    ) {
        SessionsCard(
            totalSession = uiState.totalSessionsStarted,
            completedSessions = uiState.totalSessionsCompleted,
            cancelledSessions = uiState.totalSessionsCancelled,
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            SingleValueTile(
                title = stringResource(R.string.earnings),
                value = uiState.currentDateCreditFocusPoints,
                label = stringResource(R.string.focus_points),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(spacing.medium))
            SingleValueTile(
                title = stringResource(R.string.streaks),
                value = uiState.streaksCount,
                label = stringResource(R.string.completed),
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
        }
        if (uiState.activeTimer != null && uiState.activeTimer.durationRemainingInSeconds > 0) {
            ActiveTimerSection(
                onCancelTimerClick = { onEvent(HomeEvent.CancelTimer) },
                uiState.activeTimer,
                modifier = modifier.weight(1f)
            )
        } else {
            NewTimerSection(
                selectedTimerType = uiState.selectedNewTimerDurationType,
                onStartTimerClick = { onEvent(HomeEvent.StartTimer) },
                onTimerTypeSelected = { durationType ->
                    onEvent(
                        HomeEvent.SelectNewTimerDurationType(
                            durationType
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    WriteTimeTheme {
        HomeScreen(HomeUiState(activeTimer = ActiveTimer(0, 25, 200.seconds)), {})
    }
}