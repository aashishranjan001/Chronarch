package com.aashish.writetime.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.home.presentation.ActiveTimer
import kotlin.time.Duration.Companion.seconds

@Composable
fun ActiveTimerSection(
    onCancelTimerClick: () -> Unit,
    activeTimer: ActiveTimer, modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(spacing.medium))
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Timer(
            modifier = Modifier.weight(1f),
            secondsRemaining = activeTimer.durationRemainingInSeconds,
            totalSeconds = activeTimer.duration.inWholeSeconds
        )
        Button (
            modifier = Modifier.fillMaxWidth(),
            onClick = onCancelTimerClick
        ) {
            Text(
                text = stringResource(R.string.cancel_timer),
                modifier = Modifier.padding(spacing.small),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun ActiveTimerSectionPreview() {
    WriteTimeTheme {
        ActiveTimerSection({}, ActiveTimer(2, 25, 200.seconds))
    }
}