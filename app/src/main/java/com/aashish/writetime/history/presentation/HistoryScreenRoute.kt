package com.aashish.writetime.history.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.components.NoDataScreen
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.components.FilterBottomSheet
import com.aashish.writetime.history.presentation.components.HistoryScreenTopBar
import com.aashish.writetime.history.presentation.components.SessionsHistorySection
import com.aashish.writetime.history.presentation.components.TransactionsHistorySection
import com.aashish.writetime.history.presentation.model.HistoryTab
import com.aashish.writetime.history.presentation.model.HistoryUiState
import com.aashish.writetime.redemption.presentation.redemption_corner.RedemptionCornerEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HistoryScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        pageCount = {
            uiState.tabs.size
        }
    )

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { uiEffect ->
            when (uiEffect) {
                is HistoryUiEffect.ScrollToTab -> {
                    pagerState.animateScrollToPage(uiEffect.tabIndex)
                }
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.distinctUntilChanged()
            .collectLatest { page ->
                viewModel.onEvent(HistoryEvent.SaveSelectedTab(tabIndex = page))
            }
    }

    HistoryScreen(pagerState, uiState, viewModel::onEvent, modifier)
}

@Composable
fun HistoryScreen(
    pagerState: PagerState,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    if (uiState.isLoading) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.isError) {
        NoDataScreen(
            thumbnailResId = R.drawable.app_error,
            contentDescription = stringResource(R.string.internal_error),
            title = stringResource(R.string.internal_error),
            message = stringResource(R.string.history_fetch_error_message),
            actionText = stringResource(R.string.retry),
            actionClick = {
                onEvent(HistoryEvent.RetryClick)
            })
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            HistoryScreenTopBar(
                tabList = uiState.tabs,
                currentPage = pagerState.currentPage,
                onTabClick = {
                    onEvent(HistoryEvent.TabSelect(it))
                },
                onFilterMenuClick = { onEvent(HistoryEvent.FilterMenuIconClick) }
            )

            HorizontalPager(
                state = pagerState
            ) { page ->
                when (uiState.tabs[page]) {
                    HistoryTab.SESSIONS -> SessionsHistorySection(uiState.filteredSessions, uiState.areSessionFilteredApplied)
                    HistoryTab.TRANSACTIONS -> TransactionsHistorySection(uiState.filteredTransactions, uiState.areTransactionFiltersApplied)
                }
            }
        }
    }

    if (uiState.showFilterBottomSheet) {
        FilterBottomSheet(
            categories = uiState.filterCategories,
            options = uiState.filterOptions,
            onDismiss = { onEvent(HistoryEvent.DismissFilterMenu) },
            onClearFilter = { onEvent(HistoryEvent.ClearFilter) },
            onApplyFilter = { onEvent(HistoryEvent.ApplyFilter) },
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

    val dateRangePickerState = rememberDateRangePickerState()
    if (uiState.showDatePickerDialog) {
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
            DateRangePicker(dateRangePickerState)
        }
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { HistoryTab.entries.size }
    )
    WriteTimeTheme {
        HistoryScreen(pagerState, HistoryUiState(
            isLoading = false,
            isError = false,
            tabs = HistoryTab.entries,
            selectedTab = HistoryTab.SESSIONS
        ), {})
    }
}