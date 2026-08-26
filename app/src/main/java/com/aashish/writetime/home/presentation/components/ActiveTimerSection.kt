package com.aashish.writetime.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.home.presentation.ActiveTimer
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

@Composable
fun ActiveTimerSection(
    onCancelTimerClick: () -> Unit,
    activeTimer: ActiveTimer, modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = spacing.extraSmall
        ),
        shape = RoundedCornerShape(spacing.medium),
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                )
                .fillMaxWidth()
                .padding(spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Timer(
                secondsRemaining = activeTimer.durationRemainingInSeconds,
                totalSeconds = activeTimer.duration.inWholeSeconds,
                endTime = activeTimer.idealEndTime
            )
            Spacer(modifier = Modifier.height(spacing.medium))
            OutlinedButton (
                modifier = Modifier.fillMaxWidth(),
                onClick = onCancelTimerClick,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                Text(
                    text = stringResource(R.string.stop_timer),
                    modifier = Modifier.padding(spacing.small),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveTimerSectionPreview() {
    WriteTimeTheme {
        ActiveTimerSection(
            onCancelTimerClick = {},
            activeTimer = ActiveTimer(2, 25, 200.seconds, Instant.now())
        )
    }
}