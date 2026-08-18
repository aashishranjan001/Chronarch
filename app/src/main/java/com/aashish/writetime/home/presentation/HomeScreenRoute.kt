package com.aashish.writetime.home.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.ConfirmationDialog
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.home.presentation.components.SessionsCard
import com.aashish.writetime.home.presentation.components.SingleValueTile
import com.aashish.writetime.home.presentation.components.ActiveTimerSection
import com.aashish.writetime.home.presentation.components.NewTimerSection
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeScreenRoute(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val timerCancelledMessage =
        stringResource(R.string.timer_stopped_message)

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { uiEffect ->
            when(uiEffect) {
                is HomeUiEffect.ShowTimerCancelledSnackbar -> {
                    snackbarHostState.showSnackbar(timerCancelledMessage)
                }
                else -> {}
            }
        }
    }

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
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(spacing.medium),
        verticalArrangement = Arrangement.spacedBy(space = spacing.medium)
    ) {
        item {
            SessionsCard(
                totalSession = uiState.totalSessionsStarted,
                completedSessions = uiState.totalSessionsCompleted,
                cancelledSessions = uiState.totalSessionsCancelled,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                SingleValueTile(
                    title = stringResource(R.string.earnings),
                    value = uiState.currentDateCreditFocusPoints,
                    label = stringResource(R.string.focus_points),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    foregroundColor = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(spacing.medium))
                SingleValueTile(
                    title = stringResource(R.string.streaks),
                    value = uiState.streaksCount,
                    label = stringResource(R.string.completed),
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    foregroundColor = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Column(
                modifier = Modifier.animateContentSize()
            ) {
                if (uiState.activeTimer != null && uiState.activeTimer.durationRemainingInSeconds > 0) {
                    ActiveTimerSection(
                        onCancelTimerClick = { onEvent(HomeEvent.CancelTimerClick) },
                        uiState.activeTimer
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
    }
    if (uiState.showConfirmationDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_stop_timer_title),
            message = stringResource(R.string.confirm_stop_timer_message),
            onPositiveClick = { onEvent(HomeEvent.CancelTimerConfirmed) },
            onNegativeClick = { onEvent(HomeEvent.CancelTimerDismissed) },
            positiveCtaText = stringResource(R.string.action_yes),
            negativeCtaText = stringResource(R.string.action_no),
            onDismissRequest = { onEvent(HomeEvent.CancelTimerDismissed) }
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    WriteTimeTheme {
        HomeScreen(HomeUiState(activeTimer = ActiveTimer(0, 25, 200.seconds)), {})
    }
}