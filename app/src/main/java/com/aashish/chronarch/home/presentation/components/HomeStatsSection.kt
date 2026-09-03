package com.aashish.chronarch.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.theme.ChronarchTheme

@Composable
fun HomeStatsSection(
    sessionStarted: Int,
    sessionsCompleted: Int,
    sessionsCancelled: Int,
    currentDateFocusPoints: Int,
    streaksCount: Int,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.medium, alignment = Alignment.CenterVertically),
    ) {
        Text(
            text = stringResource(R.string.title_todays_overview),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
        SessionsCard(
            totalSession = sessionStarted,
            completedSessions = sessionsCompleted,
            cancelledSessions = sessionsCancelled
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            SingleValueTile(
                title = stringResource(R.string.earnings),
                value = currentDateFocusPoints,
                label = stringResource(R.string.focus_points),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                foregroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(spacing.medium))
            SingleValueTile(
                title = stringResource(R.string.streaks),
                value = streaksCount,
                label = stringResource(R.string.completed),
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                foregroundColor = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeStatsSectionPreview() {
    ChronarchTheme {
        HomeStatsSection(
            sessionStarted = 3,
            sessionsCompleted = 1,
            sessionsCancelled = 2,
            currentDateFocusPoints = 3,
            streaksCount = 2
        )
    }

}