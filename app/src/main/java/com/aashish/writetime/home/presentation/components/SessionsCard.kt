package com.aashish.writetime.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.VerticalLabelledValue
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun SessionsCard(
    totalSession: Int,
    completedSessions: Int,
    cancelledSessions: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    foregroundColor: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = spacing.extraSmall
        ),
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            backgroundColor, backgroundColor.copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(spacing.medium),
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = stringResource(R.string.sessions),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = foregroundColor
            )
            Spacer(modifier = Modifier.height(spacing.small))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val valueTextStyle =
                    MaterialTheme.typography.headlineLarge.copy(color = foregroundColor)
                val labelTextStyle = MaterialTheme.typography.labelSmall.copy(color = foregroundColor)
                VerticalLabelledValue(
                    value = totalSession.toString(),
                    label = stringResource(R.string.started),
                    valueTextStyle = valueTextStyle,
                    labelTextStyle = labelTextStyle,
                    modifier = Modifier.weight(1f)
                )
                VerticalLabelledValue(
                    value = completedSessions.toString(),
                    label = stringResource(R.string.completed),
                    valueTextStyle = valueTextStyle,
                    labelTextStyle = labelTextStyle,
                    modifier = Modifier.weight(1f)
                )
                VerticalLabelledValue(
                    value = cancelledSessions.toString(),
                    label = stringResource(R.string.cancelled),
                    valueTextStyle = valueTextStyle,
                    labelTextStyle = labelTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun SessionsCardPreview() {
    WriteTimeTheme {
        SessionsCard(10, 7, 3)
    }
}