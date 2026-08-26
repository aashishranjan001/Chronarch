package com.aashish.writetime.home.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import java.time.Instant
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
        contentPadding = PaddingValues(spacing.medium),
        modifier = modifier
            .fillMaxSize(),
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
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = spacing.medium)
            ) {
                if (uiState.activeTimer != null && uiState.activeTimer.durationRemainingInSeconds >= 0) {
                    ActiveTimerSection(
                        onCancelTimerClick = { onEvent(HomeEvent.CancelTimerClick) },
                        activeTimer = uiState.activeTimer
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

    when(uiState.dialog) {
        HomeDialog.TimerCancelConfirmation -> {
            ConfirmationDialog(
                title = stringResource(R.string.confirm_stop_timer_title),
                message = stringResource(R.string.confirm_stop_timer_message),
                onConfirmClick = { onEvent(HomeEvent.CancelTimerConfirmed) },
                onDismissRequest = { onEvent(HomeEvent.CancelTimerDismissed) },
                confirmText = stringResource(R.string.confirm),
                dismissText = stringResource(R.string.cancel)
            )
        }
        is HomeDialog.TimerFinishedInformation -> {
            ConfirmationDialog(
                title = stringResource(R.string.timer_ended),
                message = stringResource(R.string.session_completed_message, uiState.dialog.timerDurationMins),
                onConfirmClick = { onEvent(HomeEvent.TimerCompletedDialogDismiss) },
                onDismissRequest = { onEvent(HomeEvent.TimerCompletedDialogDismiss) },
                confirmText = stringResource(R.string.awesome)
            )
        }
        null -> {}
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    WriteTimeTheme {
        HomeScreen(HomeUiState(activeTimer = ActiveTimer(0, 25, 200.seconds, Instant.now())), {})
    }
}