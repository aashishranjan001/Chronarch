package com.aashish.writetime.weekoverview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun SessionsStatsSection(
    shortSessionsSuccessCount: Int,
    totalShortSessionsCount: Int,
    longSessionsSuccessCount: Int,
    totalLongSessionsCount: Int,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(spacing.medium)
            )
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = spacing.medium,
            alignment = Alignment.CenterVertically
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SessionCompletionCountTile(
                painter = painterResource(R.drawable.short_duration_timer),
                contentDescription = stringResource(R.string.short_duration_timer),
                completedSessionCount = shortSessionsSuccessCount,
                cancelledSessionCount = totalShortSessionsCount - shortSessionsSuccessCount,
                backgroundColor = MaterialTheme.colorScheme.primaryFixed,
                modifier = Modifier.weight(1f)
            )
            SessionCompletionCountTile(
                painter = painterResource(R.drawable.long_duration_timer),
                contentDescription = stringResource(R.string.long_duration_timer),
                completedSessionCount = longSessionsSuccessCount,
                cancelledSessionCount = totalLongSessionsCount - longSessionsSuccessCount,
                backgroundColor = MaterialTheme.colorScheme.secondaryFixed,
                modifier = Modifier.weight(1f)
            )
            SessionCompletionCountTile(
                painter = painterResource(R.drawable.all_duration_timer),
                contentDescription = stringResource(R.string.all_duration_timer),
                completedSessionCount = shortSessionsSuccessCount + longSessionsSuccessCount,
                cancelledSessionCount = totalShortSessionsCount + totalLongSessionsCount - shortSessionsSuccessCount - longSessionsSuccessCount,
                backgroundColor = MaterialTheme.colorScheme.tertiaryFixed,
                modifier = Modifier.weight(1f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            if (totalShortSessionsCount != 0) { // to progress to be measured when denominator is 0
                ProgressIndicatorBox(
                    value = shortSessionsSuccessCount,
                    total = totalShortSessionsCount,
                    backgroundColor = MaterialTheme.colorScheme.primaryFixed,
                    foregroundColor = MaterialTheme.colorScheme.onPrimaryFixed,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight(totalShortSessionsCount.toFloat() / (totalShortSessionsCount + totalLongSessionsCount))
                        .align(Alignment.BottomStart)
                )
            }
            if (totalLongSessionsCount != 0) { // to progress to be measured when denominator is 0
                ProgressIndicatorBox(
                    value = longSessionsSuccessCount,
                    total = totalLongSessionsCount,
                    backgroundColor = MaterialTheme.colorScheme.secondaryFixed,
                    foregroundColor = MaterialTheme.colorScheme.onSecondaryFixed,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight(totalLongSessionsCount.toFloat() / (totalShortSessionsCount + totalLongSessionsCount))
                        .align(Alignment.BottomCenter)
                )
            }
            if (totalShortSessionsCount + totalLongSessionsCount != 0) { // to progress to be measured when denominator is 0
                ProgressIndicatorBox(
                    value = shortSessionsSuccessCount + longSessionsSuccessCount,
                    total = totalShortSessionsCount + totalLongSessionsCount,
                    backgroundColor = MaterialTheme.colorScheme.tertiaryFixed,
                    foregroundColor = MaterialTheme.colorScheme.onTertiaryFixed,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight()
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }


}

@Composable
fun SessionCompletionCountTile(
    painter: Painter,
    contentDescription: String,
    completedSessionCount: Int,
    cancelledSessionCount: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(spacing.medium))
            .background(backgroundColor)
            .height(180.dp)
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = contentDescription,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(LocalSpacing.current.small))
        Text(
            text = buildAnnotatedString {
                append("$completedSessionCount ")
                appendInlineContent(id = "completed_icon", alternateText = stringResource(R.string.finished_session))
            },
            inlineContent = mapOf(
                "completed_icon" to InlineTextContent(
                    placeholder = Placeholder(
                        width = 1.2.em,
                        height = 1.2.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.finished_session),
                        tint = Color.Green
                    )
                }
            )
        )
        Text(
            text = buildAnnotatedString {
                append("$cancelledSessionCount ")
                appendInlineContent(id = "cancelled_icon", alternateText = stringResource(R.string.cancelled_session))
            },
            inlineContent = mapOf(
                "cancelled_icon" to InlineTextContent(
                    placeholder = Placeholder(
                        width = 1.2.em,
                        height = 1.2.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cancelled_session),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            )
        )
    }
}

@Preview
@Composable
private fun SessionsStatsSectionPreview() {
    WriteTimeTheme {
        SessionsStatsSection(8, 9, 3, 7)
    }
}