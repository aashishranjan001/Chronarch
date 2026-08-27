package com.aashish.chronarch.home.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.chronarch.common.domain.model.DurationType
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.home.presentation.ActiveTimer
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeTimerSection(
    activeTimer: ActiveTimer? = null,
    selectedTimerDurationType: DurationType,
    onStartTimer: () -> Unit,
    onTimerTypeSelected: (DurationType) -> Unit,
    onCancelTimerClick: () -> Unit,
    modifier: Modifier = Modifier) {

    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .widthIn(max = 500.dp)
            .fillMaxWidth()
            .animateContentSize()
            .padding(bottom = spacing.medium) // to prevent bottom shadow truncation from animateContentSize
    ) {
        if (activeTimer != null && activeTimer.durationRemainingInSeconds >= 0) {
            ActiveTimerSection(
                onCancelTimerClick = onCancelTimerClick,
                activeTimer = activeTimer
            )
        } else {
            NewTimerSection(
                selectedTimerType = selectedTimerDurationType,
                onStartTimerClick = onStartTimer,
                onTimerTypeSelected = { onTimerTypeSelected(it) }
            )
        }
    }
}

@Preview
@Composable
private fun HomeTimerSectionPreview() {
    ChronarchTheme {
        HomeTimerSection(
            activeTimer = ActiveTimer(0, 25, 200.seconds, Instant.now()),
            selectedTimerDurationType = DurationType.ShortDuration,
            onStartTimer = {},
            onCancelTimerClick = {},
            onTimerTypeSelected = {}
        )
    }
}