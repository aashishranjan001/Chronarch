package com.aashish.writetime.history.presentation

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.components.FilterBottomSheet
import com.aashish.writetime.history.presentation.components.HistoryScreenTopBar
import com.aashish.writetime.history.presentation.components.SessionsHistorySection
import com.aashish.writetime.history.presentation.components.TransactionsHistorySection
import com.aashish.writetime.history.presentation.model.FilterCategory
import com.aashish.writetime.history.presentation.model.FilterOption
import com.aashish.writetime.history.presentation.model.FilterOptionType
import com.aashish.writetime.history.presentation.model.HistoryTab
import com.aashish.writetime.history.presentation.model.HistoryUiState
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

    if (uiState.showFilterBottomSheet) {
        FilterBottomSheet(
            categories = buildMap {
                uiState.draftSessionsFilters?.let {
                    put(FilterCategory.SessionFilter.CompletionStatus, it.appliedCompletionStatusFilters.isNotEmpty())
                    put(FilterCategory.SessionFilter.DurationType, it.appliedDurationTypeFilters.isNotEmpty())
                    put(FilterCategory.Date, (it.appliedDateFilter != null))
                }

                uiState.draftTransactionsFilters?.let {
                    put(FilterCategory.TransactionFilter.Type, it.appliedTypeFilters.isNotEmpty())
                    put(FilterCategory.Date, (it.appliedDateFilter != null))
                }
            },
            options = buildList {
                uiState.draftSessionsFilters?.selectedFilterCategory?.let {
                    when (it) {
                        FilterCategory.Date -> {
                            val appliedDateFilter = uiState.draftSessionsFilters.appliedDateFilter
                            add(
                                FilterOptionType.Radio(FilterOption.Date.ThisWeek, appliedDateFilter is FilterOption.Date.ThisWeek)
                            )
                            add(
                                FilterOptionType.Radio(FilterOption.Date.ThisMonth, appliedDateFilter is FilterOption.Date.ThisMonth)
                            )
                            (appliedDateFilter as? FilterOption.Date.CustomRange).let { dateRange ->
                                add(
                                    FilterOptionType.DateRange(dateRange?.startDate, dateRange?.endDate)
                                )
                            }
                        }
                        FilterCategory.SessionFilter.CompletionStatus -> {
                            FilterOption.SessionFilter.CompletionStatus.entries.forEach { completionStatusFilterOption ->
                                add(
                                    FilterOptionType.Checkbox(completionStatusFilterOption, uiState.draftSessionsFilters.appliedCompletionStatusFilters.contains(completionStatusFilterOption))
                                )
                            }
                        }
                        FilterCategory.SessionFilter.DurationType -> {
                            FilterOption.SessionFilter.DurationType.entries.forEach { durationTypeFilterOption ->
                                add(
                                    FilterOptionType.Checkbox(durationTypeFilterOption, uiState.draftSessionsFilters.appliedDurationTypeFilters.contains(durationTypeFilterOption))
                                )
                            }
                        }
                        else -> {}
                    }
                }
                uiState.draftTransactionsFilters?.selectedFilterCategory?.let {
                    when(it) {
                        FilterCategory.TransactionFilter.Type -> {
                            FilterOption.TransactionFilter.Type.entries.forEach { transactionTypeFilterOption ->
                                add(
                                    FilterOptionType.Checkbox(transactionTypeFilterOption, uiState.draftTransactionsFilters.appliedTypeFilters.contains(transactionTypeFilterOption))
                                )
                            }
                        }
                        FilterCategory.Date -> {
                            val appliedDateFilter = uiState.draftTransactionsFilters.appliedDateFilter
                            add(
                                FilterOptionType.Radio(FilterOption.Date.ThisWeek, appliedDateFilter is FilterOption.Date.ThisWeek)
                            )
                            add(
                                FilterOptionType.Radio(FilterOption.Date.ThisMonth, appliedDateFilter is FilterOption.Date.ThisMonth)
                            )
                            (appliedDateFilter as? FilterOption.Date.CustomRange).let { dateRange ->
                                add(
                                    FilterOptionType.DateRange(dateRange?.startDate, dateRange?.endDate)
                                )
                            }
                        }
                        else -> {}
                    }
                }
            },
            selectedCategory = uiState.draftSessionsFilters?.selectedFilterCategory ?: uiState.draftTransactionsFilters?.selectedFilterCategory,
            onDismiss = { onEvent(HistoryEvent.DismissFilterMenu) },
            onClearFilter = { onEvent(HistoryEvent.ClearFilter) },
            onApplyFilter = { onEvent(HistoryEvent.ApplyFilter) },
            onFilterCategoryClick = { category ->
                onEvent(
                    HistoryEvent.FilterCategorySelected(category)
                )
            },
            onFilterOptionClick = { option, isSelected ->
                onEvent(
                    HistoryEvent.FilterOptionSelected(option, isSelected)
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
        HistoryScreen(pagerState, HistoryUiState(HistoryTab.entries, HistoryTab.SESSIONS), {})
    }
}