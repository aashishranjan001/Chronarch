package com.aashish.chronarch.common.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Create a reusable Modifier to draw stripes
fun Modifier.stripedBackground(
    stripeColor: Color,
    backgroundColor: Color,
    stripeWidth: Dp = 10.dp
): Modifier = this.drawWithCache {
    val stripeWidthPx = stripeWidth.toPx()
    val totalWidth = stripeWidthPx * 2 // Pattern cycle width

    // Use hard color stops (0.5f to 0.5f) for distinct edges
    val brush = Brush.linearGradient(
        0.0f to stripeColor,
        0.5f to stripeColor,
        0.5f to backgroundColor,
        1.0f to backgroundColor,
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(totalWidth, totalWidth),
        tileMode = TileMode.Repeated // Tile the pattern
    )

    onDrawBehind {
        drawRect(brush = brush)
    }
}