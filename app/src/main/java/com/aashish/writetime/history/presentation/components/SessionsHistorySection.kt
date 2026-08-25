package com.aashish.writetime.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.calculatePercent
import com.aashish.writetime.common.ui.components.NoDataScreen
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.common.ui.toHumanReadableDurationFormat
import com.aashish.writetime.common.ui.toReadableLocalDateTime
import com.aashish.writetime.history.presentation.model.SessionHistoryItem

@Composable
fun SessionsHistorySection(
    sessionList: List<SessionHistoryItem>,
    areFiltersApplied: Boolean,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (sessionList.isEmpty()) {
        NoDataScreen(
            thumbnailResId = if (areFiltersApplied) R.drawable.no_filtered_session_history else R.drawable.no_sessions_found,
            contentDescription = stringResource(R.string.no_session_history_found_title),
            title = stringResource(R.string.no_session_history_found_title),
            message = stringResource(
                if (areFiltersApplied) R.string.no_filtered_session_history_found_description else
                    R.string.no_session_history_found_description
            ),
            actionText = stringResource(R.string.reset_filter).takeIf { areFiltersApplied },
            actionClick = onResetFilters
        )
    } else {
        val spacing = LocalSpacing.current
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            items(items = sessionList, key = { it.id }) { session ->
                SessionHistoryItemContent(
                    targetDuration = toHumanReadableDurationFormat(session.durationType.duration.inWholeSeconds),
                    runDuration = toHumanReadableDurationFormat(session.runDuration.inWholeSeconds),
                    startTime = session.startTime.toReadableLocalDateTime(),
                    percentageCompleted = calculatePercent(
                        session.runDuration.inWholeSeconds.toDouble(),
                        session.durationType.duration.inWholeSeconds.toDouble()
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun SessionsHistorySectionPreview() {
    WriteTimeTheme {
        SessionsHistorySection(sessionList = emptyList(), areFiltersApplied = false, onResetFilters = {})
    }
}