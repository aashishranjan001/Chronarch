package com.aashish.writetime.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.VerticalLabelledValue
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun SingleValueTile(
    title: String,
    value: Int,
    label: String,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(spacing.medium))
            .background(backgroundColor)
            .padding(spacing.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.height(spacing.small))
        VerticalLabelledValue(
            value = value.toString(),
            label = label,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun SingleValueTilePreview() {
    WriteTimeTheme {
        SingleValueTile(title = "Earnings", value = 7, label = "Focus Points")
    }
}