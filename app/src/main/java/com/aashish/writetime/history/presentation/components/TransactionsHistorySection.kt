package com.aashish.writetime.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.NoDataScreen
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.common.ui.toReadableLocalDateTime
import com.aashish.writetime.history.presentation.model.TransactionHistoryItem

@Composable
fun TransactionsHistorySection(
    transactionList: List<TransactionHistoryItem>,
    areFiltersApplied: Boolean,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (transactionList.isEmpty()) {
        NoDataScreen(
            thumbnailResId = if (areFiltersApplied) R.drawable.no_filtered_transactions_found else R.drawable.no_transactions_found,
            contentDescription = stringResource(R.string.no_transaction_history_found_title),
            title = stringResource(R.string.no_transaction_history_found_title),
            message = stringResource(
                if (areFiltersApplied)
                    R.string.no_filtered_transaction_history_found_description
                else
                    R.string.no_transaction_history_found_description
            ),
            actionText = stringResource(R.string.reset_filter).takeIf { areFiltersApplied },
            actionClick = onResetFilters
        )
    } else {
        val spacing = LocalSpacing.current
        LazyColumn(
            contentPadding = PaddingValues(spacing.medium),
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            items(items = transactionList, key = { it.id }) { transaction ->
                TransactionHistoryItemContent(
                    message = transaction.message,
                    startTime = transaction.timestamp.toReadableLocalDateTime(),
                    amount = transaction.value,
                    transactionType = transaction.transactionType
                )
            }
        }
    }
}

@Preview
@Composable
private fun TransactionsHistorySectionPreview() {
    WriteTimeTheme {
        TransactionsHistorySection(
            transactionList = emptyList(),
            areFiltersApplied = false,
            onResetFilters = {})
    }
}