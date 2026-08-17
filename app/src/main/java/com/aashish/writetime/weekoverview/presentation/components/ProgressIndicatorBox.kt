package com.aashish.writetime.weekoverview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.stripedBackground
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun ProgressIndicatorBox(
    value: Int,
    total: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    foregroundColor: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = foregroundColor,
                shape = RoundedCornerShape(spacing.large)
            )
            .clip(
                RoundedCornerShape(spacing.large)
            )
            .clip(RoundedCornerShape(spacing.large))
            .background(foregroundColor)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(0.5f), backgroundColor
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .padding(spacing.small)
                .border(
                    width = 2.dp,
                    color = foregroundColor,
                    shape = RoundedCornerShape(spacing.large)
                )
                .clip(
                    RoundedCornerShape(spacing.large)
                )
                .fillMaxHeight(value.toFloat() / total)
                .fillMaxWidth()
                .stripedBackground(foregroundColor, Color.Transparent, 2.dp)
                .align(
                    Alignment.BottomCenter
                )
        )
    }
}



@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun ProgressIndicatorBoxPreview() {
    WriteTimeTheme {
        ProgressIndicatorBox(
            120, 200
        )
    }

}