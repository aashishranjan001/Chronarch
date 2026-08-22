package com.aashish.writetime.history.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.components.FilterBottomSheet
import com.aashish.writetime.history.presentation.components.HistoryScreenTopBar
import com.aashish.writetime.history.presentation.components.SessionsHistorySection
import com.aashish.writetime.history.presentation.components.TransactionsHistorySection
import com.aashish.writetime.history.presentation.model.FilterCategory
import com.aashish.writetime.history.presentation.model.FilterOption
import com.aashish.writetime.history.presentation.model.FilterOptionState
import com.aashish.writetime.history.presentation.model.HistoryTab
import com.aashish.writetime.history.presentation.model.HistoryUiState

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
            onFilterMenuClick = {
                when (uiState.tabs[pagerState.currentPage]) {
                    HistoryTab.SESSIONS -> onEvent(HistoryEvent.SessionsTabFilterMenuClick)
                    HistoryTab.TRANSACTIONS -> onEvent(HistoryEvent.TransactionsTabFilterMenuClick)
                }
            }
        )

        HorizontalPager(
            state = pagerState
        ) { page ->
            when (uiState.tabs[page]) {
                HistoryTab.SESSIONS -> SessionsHistorySection(uiState.filteredSessions)
                HistoryTab.TRANSACTIONS -> TransactionsHistorySection(uiState.filteredTransactions)
            }
        }
    }

    uiState.draftSessionsFilters?.let { draftSessionFilters ->
        FilterBottomSheet(
            categories = FilterCategory.SessionFilter.entries.associate { sessionCategory ->
                when (sessionCategory) {
                    FilterCategory.SessionFilter.CompletionStatus -> sessionCategory to draftSessionFilters.appliedCompletionStatusFilters.isNotEmpty()
                    FilterCategory.SessionFilter.Date -> sessionCategory to (draftSessionFilters.appliedDateFilter != null)
                    FilterCategory.SessionFilter.DurationType -> sessionCategory to draftSessionFilters.appliedDurationTypeFilters.isNotEmpty()
                }
            },
            options = when (draftSessionFilters.selectedFilterCategory) {
                FilterCategory.SessionFilter.CompletionStatus -> {
                    FilterOption.SessionFilter.CompletionStatus.entries.map {
                        FilterOptionState.SelectionFilterOptionState(
                            option = it,
                            isSelected = draftSessionFilters.appliedCompletionStatusFilters.contains(
                                it
                            )
                        )
                    }
                }

                FilterCategory.SessionFilter.Date -> {
                    emptyList()
                }

                FilterCategory.SessionFilter.DurationType -> {
                    FilterOption.SessionFilter.DurationType.entries.map {
                        FilterOptionState.SelectionFilterOptionState(
                            option = it,
                            isSelected = draftSessionFilters.appliedDurationTypeFilters.contains(it)
                        )
                    }
                }

                null -> emptyList()
            },
            selectedCategory = draftSessionFilters.selectedFilterCategory,
            onDismiss = { onEvent(HistoryEvent.DismissSessionsFilterMenu) },
            onClearFilter = { onEvent(HistoryEvent.ClearSessionsFilter) },
            onApplyFilter = { onEvent(HistoryEvent.ApplySessionsFilter) },
            onFilterCategoryClick = { category ->
                onEvent(
                    HistoryEvent.FilterCategorySelected(
                        category
                    )
                )
            },
            onFilterOptionClick = { option, isSelected ->
                onEvent(
                    HistoryEvent.FilterOptionSelected(
                        option,
                        isSelected
                    )
                )
            }
        )
    }

    uiState.draftTransactionsFilters?.let { draftTransactionsFilters ->
        FilterBottomSheet(
            categories = FilterCategory.TransactionFilter.entries.associate { transactionsCategory ->
                when (transactionsCategory) {
                    FilterCategory.TransactionFilter.Type -> {
                        transactionsCategory to draftTransactionsFilters.appliedTypeFilters.isNotEmpty()
                    }

                    FilterCategory.TransactionFilter.Date -> {
                        transactionsCategory to (draftTransactionsFilters.appliedDateFilter != null)
                    }
                }
            },
            options = when (draftTransactionsFilters.selectedFilterCategory) {
                FilterCategory.TransactionFilter.Date -> emptyList()
                FilterCategory.TransactionFilter.Type -> {
                    FilterOption.TransactionFilter.Type.entries.map {
                        FilterOptionState.SelectionFilterOptionState(
                            option = it,
                            isSelected = draftTransactionsFilters.appliedTypeFilters.contains(it)
                        )
                    }
                }

                null -> emptyList()
            },
            selectedCategory = draftTransactionsFilters.selectedFilterCategory,
            onDismiss = { onEvent(HistoryEvent.DismissTransactionsFilterMenu) },
            onClearFilter = { onEvent(HistoryEvent.ClearTransactionsFilter) },
            onApplyFilter = { onEvent(HistoryEvent.ApplyTransactionsFilter) },
            onFilterCategoryClick = { category ->
                onEvent(
                    HistoryEvent.FilterCategorySelected(
                        category
                    )
                )
            },
            onFilterOptionClick = { option, isSelected ->
                onEvent(
                    HistoryEvent.FilterOptionSelected(
                        option,
                        isSelected
                    )
                )
            }
        )
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
        HistoryScreen(pagerState, HistoryUiState(HistoryTab.entries), {})
    }
}