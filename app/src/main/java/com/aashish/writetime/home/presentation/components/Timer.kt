package com.aashish.writetime.home.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.formatDurationHhMmSs
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.common.ui.toHumanReadableDurationFormat

@Composable
fun Timer(
    secondsRemaining: Long,
    totalSeconds: Long,
    modifier: Modifier = Modifier) {

    val progress = remember {
        Animatable(initialValue = secondsRemaining.toFloat() / totalSeconds)
    }

    LaunchedEffect(secondsRemaining, totalSeconds) {
        progress.animateTo(
            targetValue = (secondsRemaining.toFloat() / totalSeconds).coerceIn(0f, 1f),
        )
    }

    val outlineColor = MaterialTheme.colorScheme.primaryFixedDim
    val progressRimColor = MaterialTheme.colorScheme.primary


    Box(modifier = modifier.aspectRatio(1f).padding(LocalSpacing.current.medium).drawWithContent{
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
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = formatDurationHhMmSs(secondsRemaining), style = MaterialTheme.typography.displayMedium)
            Spacer(modifier = Modifier.height(LocalSpacing.current.small))
            Text(text = toHumanReadableDurationFormat(totalSeconds), style = MaterialTheme.typography.bodyLarge)
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun TimerPreview() {
    WriteTimeTheme {
        Timer(20L, 200L)
    }
}