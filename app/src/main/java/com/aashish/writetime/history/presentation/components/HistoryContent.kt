package com.aashish.writetime.history.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.HistoryEvent
import com.aashish.writetime.history.presentation.model.HistoryTab
import com.aashish.writetime.history.presentation.model.HistoryUiState

@Composable
fun HistoryContent(
    pagerState: PagerState,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier) {

    Column(modifier = modifier.fillMaxSize()) {
        HistoryScreenTopBar(
            tabList = uiState.tabs,
            currentPage = pagerState.currentPage,
            showFilterAppliedBadge = uiState.showFilterAppliedBadge,
            onTabClick = {
                onEvent(HistoryEvent.TabSelect(it))
            },
            onFilterMenuClick = { onEvent(HistoryEvent.FilterMenuIconClick) },
        )

        HorizontalPager(
            state = pagerState
        ) { page ->
            when (uiState.tabs[page]) {
                HistoryTab.SESSIONS -> SessionsHistorySection(
                    sessionList = uiState.filteredSessions,
                    areFiltersApplied = uiState.areSessionFilteredApplied,
                    onResetFilters = { onEvent(HistoryEvent.ResetAppliedFilters) })

                HistoryTab.TRANSACTIONS -> TransactionsHistorySection(
                    transactionList = uiState.filteredTransactions,
                    areFiltersApplied = uiState.areTransactionFiltersApplied,
                    onResetFilters = { onEvent(HistoryEvent.ResetAppliedFilters) })
            }
        }
    }

    if (uiState.showFilterBottomSheet) {
        FilterBottomSheet(
            categories = uiState.filterCategories,
            options = uiState.filterOptions,
            onDismiss = { onEvent(HistoryEvent.DismissFilterMenu) },
            onClearFilter = { onEvent(HistoryEvent.ClearDraftFilters) },
            onApplyFilter = { onEvent(HistoryEvent.ApplyDraftFilters) },
            onFilterCategoryClick = { category ->
                onEvent(
                    HistoryEvent.FilterCategorySelected(category)
                )
            },
            onFilterCheckboxOptionClick = { option, isSelected ->
                onEvent(
                    HistoryEvent.FilterCheckboxOptionSelected(option, isSelected)
                )
            },
            onFilterRadioOptionClick = { option ->
                onEvent(
                    HistoryEvent.FilterRadioOptionSelected(option)
                )
            },
            onCustomDateRangeClicked = {
                onEvent(
                    HistoryEvent.DateRangeFilterOptionClick
                )
            }
        )
    }

    if (uiState.showDatePickerDialog) {
        val dateRangePickerState = rememberDateRangePickerState()
        DatePickerDialog(
            onDismissRequest = { onEvent(HistoryEvent.DateRangePickerDismissed) },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(HistoryEvent.DaterFilterApplied(
                        dateRangePickerState.selectedStartDateMillis,
                        dateRangePickerState.selectedEndDateMillis
                    ))
                }) {
                    Text(text = stringResource(R.string.confirm))
                }

            },
            dismissButton = {
                TextButton(
                    onClick = { onEvent(HistoryEvent.DateRangePickerDismissed) }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = null
            )
        }
    }
}

@Preview
@Composable
private fun HistoryContentPreview() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { HistoryTab.entries.size }
    )
    WriteTimeTheme {
        HistoryContent(
            pagerState, HistoryUiState(
                isLoading = false,
                isError = false,
                tabs = HistoryTab.entries,
                selectedTab = HistoryTab.SESSIONS
            ), {})
    }
}

