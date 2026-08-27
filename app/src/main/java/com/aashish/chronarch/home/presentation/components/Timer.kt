package com.aashish.chronarch.home.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.formatDurationHhMmSs
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.common.ui.toHumanReadableDurationFormat
import com.aashish.chronarch.common.ui.toReadableLocalTime
import java.time.Instant

@Composable
fun Timer(
    secondsRemaining: Long,
    totalSeconds: Long,
    endTime: Instant,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    progressRimColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier) {

    val spacing = LocalSpacing.current

    val progress = remember {
        Animatable(initialValue = secondsRemaining.toFloat() / totalSeconds)
    }

    LaunchedEffect(secondsRemaining, totalSeconds) {
        progress.animateTo(
            targetValue = (secondsRemaining.toFloat() / totalSeconds).coerceIn(0f, 1f),
        )
    }

    Box(modifier = modifier.aspectRatio(1f).padding(spacing.medium).drawWithContent{
        drawArc(
            color = outlineColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(
                width = 5.dp.toPx(),
            )
        )
        drawArc(
            color = progressRimColor,
            startAngle = -90f,
            sweepAngle = 360f * progress.value,
            useCenter = false,
            style = Stroke(
                width = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
        drawContent()
    },
        contentAlignment = Alignment.Center
        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(space = spacing.small, alignment = Alignment.CenterVertically)) {
            Text(text = toHumanReadableDurationFormat(totalSeconds), style = MaterialTheme.typography.titleMedium)
            Text(text = formatDurationHhMmSs(secondsRemaining), style = MaterialTheme.typography.displayMedium)
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(spacing.small)) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = stringResource(R.string.end_time))
                Spacer(modifier = Modifier.width(spacing.extraSmall))
                Text(text = endTime.toReadableLocalTime(), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun TimerPreview() {
    ChronarchTheme {
        Timer(20L, 200L, Instant.now())
    }
}