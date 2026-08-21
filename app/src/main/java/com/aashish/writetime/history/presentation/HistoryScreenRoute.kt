package com.aashish.writetime.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.writetime.R
import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.calculatePercent
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.common.ui.toHumanReadableDurationFormat
import com.aashish.writetime.common.ui.toReadableLocalTime
import com.aashish.writetime.history.domain.model.EndedTimerSession
import com.aashish.writetime.history.presentation.components.NoDataFoundContent
import com.aashish.writetime.history.presentation.components.SessionHistoryItemContent
import com.aashish.writetime.history.presentation.components.TransactionHistoryItemContent
import com.aashish.writetime.history.presentation.model.HistoryTabItem
import com.aashish.writetime.history.presentation.model.HistoryUiState

@Composable
fun HistoryScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreen(uiState, viewModel::onEvent, modifier)
}

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = uiState.selectedTabIndex) {
            HistoryTabItem.entries.forEachIndexed { index, item ->
                Tab(
                    selected = uiState.selectedTabIndex == index,
                    onClick = {
                        onEvent(HistoryEvent.TabSelect(index))
                    },
                    text = {
                        Text(text = stringResource(item.titleRes))
                    }
                )
            }
        }
        when (uiState.selectedTabIndex) {
            HistoryTabItem.SESSIONS.ordinal -> SessionsSection(uiState.sessions)
            HistoryTabItem.TRANSACTION.ordinal -> TransactionsSection(uiState.transactions)
        }
    }
}

@Composable
fun SessionsSection(
    sessionList: List<EndedTimerSession>,
    modifier: Modifier = Modifier
) {

    if (sessionList.isEmpty()) {
        NoDataFoundContent(
            thumbnailResId = R.drawable.no_sessions_found,
            title = stringResource(R.string.no_session_history_found_title),
            message = stringResource(R.string.no_session_history_found_description)
        )
    } else {
        val spacing = LocalSpacing.current
        LazyColumn(
            modifier = modifier
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            items(items = sessionList, key = { it.id }) { session ->
                SessionHistoryItemContent(
                    targetDuration = toHumanReadableDurationFormat(session.durationType.duration.inWholeSeconds),
                    runDuration = toHumanReadableDurationFormat(session.runDuration.inWholeSeconds),
                    startTime = session.startTime.toReadableLocalTime(),
                    percentageCompleted = calculatePercent(
                        session.runDuration.inWholeSeconds.toDouble(),
                        session.durationType.duration.inWholeSeconds.toDouble()
                    )
                )
            }
        }
    }
}

@Composable
fun TransactionsSection(
    transactionList: List<FocusPointTransaction>,
    modifier: Modifier = Modifier
) {
    if (transactionList.isEmpty()) {
        NoDataFoundContent(
            thumbnailResId = R.drawable.no_transactions_found,
            title = stringResource(R.string.no_transaction_history_found_title),
            message = stringResource(R.string.no_transaction_history_found_description)
        )
    } else {
        val spacing = LocalSpacing.current
        LazyColumn(
            modifier = modifier
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            items(items = transactionList, key = { it.id }) { transaction ->
                TransactionHistoryItemContent(
                    message = transaction.message,
                    startTime = transaction.timestamp.toReadableLocalTime(),
                    amount = transaction.value,
                    transactionType = transaction.transactionType
                )
            }
        }
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    WriteTimeTheme {
        HistoryScreen(HistoryUiState(), {})
    }
}