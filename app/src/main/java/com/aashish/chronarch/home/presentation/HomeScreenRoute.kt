package com.aashish.chronarch.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.components.ConfirmationDialog
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.home.presentation.components.HomeStatsSection
import com.aashish.chronarch.home.presentation.components.HomeTimerSection
import com.aashish.chronarch.home.services.TimerForegroundService
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeScreenRoute(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val timerCancelledMessage =
        stringResource(R.string.timer_stopped_message)

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { uiEffect ->
            when(uiEffect) {
                is HomeUiEffect.ShowTimerCancelledSnackbar -> { snackbarHostState.showSnackbar(timerCancelledMessage) }
                is HomeUiEffect.StartTimerNotification -> {
                    ContextCompat.startForegroundService(context, TimerForegroundService.getIntent(context))
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

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass

    if(windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)) {
        LargeHeightHomeScreen(uiState, onEvent, modifier)
    } else {
        CompactHeightHomeScreen(uiState, onEvent, modifier)
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

@Composable
fun CompactHeightHomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Row(modifier = modifier.fillMaxSize().padding(spacing.medium)) {
        HomeStatsSection(
            sessionStarted = uiState.totalSessionsStarted,
            sessionsCompleted = uiState.totalSessionsCompleted,
            sessionsCancelled = uiState.totalSessionsCancelled,
            currentDateFocusPoints = uiState.currentDateTotalCreditFocusPoints,
            streaksCount = uiState.streaksCount,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(spacing.medium))
        HomeTimerSection(
            activeTimer = uiState.activeTimer,
            selectedTimerDurationType = uiState.selectedNewTimerDurationType,
            onStartTimer = { onEvent(HomeEvent.StartTimer) },
            onCancelTimerClick = { onEvent(HomeEvent.CancelTimerClick) },
            onTimerTypeSelected = {
                onEvent(HomeEvent.SelectNewTimerDurationType(it))
            },
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun LargeHeightHomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current
    LazyColumn(
        contentPadding = PaddingValues(spacing.medium),
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space = spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            HomeStatsSection(
                sessionStarted = uiState.totalSessionsStarted,
                sessionsCompleted = uiState.totalSessionsCompleted,
                sessionsCancelled = uiState.totalSessionsCancelled,
                currentDateFocusPoints = uiState.currentDateTotalCreditFocusPoints,
                streaksCount = uiState.streaksCount,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            HomeTimerSection(
                activeTimer = uiState.activeTimer,
                selectedTimerDurationType = uiState.selectedNewTimerDurationType,
                onStartTimer = { onEvent(HomeEvent.StartTimer) },
                onCancelTimerClick = { onEvent(HomeEvent.CancelTimerClick) },
                onTimerTypeSelected = {
                    onEvent(HomeEvent.SelectNewTimerDurationType(it))
                }
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    ChronarchTheme {
        HomeScreen(HomeUiState(activeTimer = ActiveTimer(0, 25, 200.seconds, Instant.now())), {})
    }
}