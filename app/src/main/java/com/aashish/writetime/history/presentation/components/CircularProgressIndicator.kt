package com.aashish.writetime.history.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun CircularProgressIndicator(
    value: String,
    progressPercent: Double,
    strokeColor: Color = MaterialTheme.colorScheme.outline,
    outlineColor: Color = MaterialTheme.colorScheme.outlineVariant,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .drawWithContent {
                drawArc(
                    color = outlineColor,
                    startAngle = 0f,
                    useCenter = false,
                    sweepAngle = 360f,
                    style = Stroke(
                        width = 2.dp.toPx()
                    )
                )
                drawArc(
                    color = strokeColor,
                    startAngle = -90f,
                    useCenter = false,
                    sweepAngle = 360f * 0.01f * progressPercent.toFloat(),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
                drawContent()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = valueStyle,
            modifier = Modifier.padding(LocalSpacing.current.small),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircularProgressIndicatorPreview() {
    WriteTimeTheme {
        CircularProgressIndicator(
            "30 min", 67.0
        )
    }
}