package com.aashish.chronarch.home.presentation.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.components.VerticalLabelledValue
import com.aashish.chronarch.common.ui.theme.ChronarchTheme

@Composable
fun SingleValueTile(
    title: String,
    value: Int,
    label: String,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    foregroundColor: Color = MaterialTheme.colorScheme.onSecondary,
    modifier: Modifier = Modifier
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
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            backgroundColor, backgroundColor.copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(spacing.medium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = foregroundColor
            )
            Spacer(modifier = Modifier.height(spacing.small))
            VerticalLabelledValue(
                value = value.toString(),
                label = label,
                valueTextStyle = MaterialTheme.typography.headlineLarge.copy(color = foregroundColor),
                labelTextStyle = MaterialTheme.typography.labelSmall.copy(color = foregroundColor),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun SingleValueTilePreview() {
    ChronarchTheme {
        SingleValueTile(title = "Earnings", value = 7, label = "Focus Points")
    }
}