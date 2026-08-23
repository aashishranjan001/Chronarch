package com.aashish.writetime.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.writetime.common.ui.isEmptyOrContains
import com.aashish.writetime.common.ui.toLocalDate
import com.aashish.writetime.common.ui.updateSet
import com.aashish.writetime.history.domain.usecase.GetHistoryUseCase
import com.aashish.writetime.history.presentation.HistoryUiEffect.*
import com.aashish.writetime.history.presentation.model.FilterCategory
import com.aashish.writetime.history.presentation.model.FilterOption
import com.aashish.writetime.history.presentation.model.HistoryTab
import com.aashish.writetime.history.presentation.model.HistoryUiState
import com.aashish.writetime.history.presentation.model.SessionsFilterUiState
import com.aashish.writetime.history.presentation.model.TransactionsFilterUiState
import com.aashish.writetime.history.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCase: GetHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HistoryUiState(
            tabs = HistoryTab.entries,
            selectedTab = HistoryTab.SESSIONS
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<HistoryUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()


    init {
        viewModelScope.launch {
            historyUseCase().collectLatest { history ->
                val allTransactions =
                    history.transactions.map { transaction -> transaction.toUiModel() }
                val allSessions = history.sessions.map { session -> session.toUiModel() }
                _uiState.update { uiState ->
                    uiState.copy(
                        allTransactions = allTransactions,
                        allSessions = allSessions,
                        filteredSessions = allSessions.filter {
                            _uiState.value.appliedSessionsFilters.appliedCompletionStatusFilters.isEmptyOrContains(
                                it.toCompletionStatusFilterLabel()
                            )
                                    && _uiState.value.appliedSessionsFilters.appliedDurationTypeFilters.isEmptyOrContains(
                                it.toDurationFilterLabel()
                            )
                                    && meetsDateFilterConstraint(_uiState.value.appliedSessionsFilters.appliedDateFilter, it.startTime)
                        },
                        filteredTransactions = allTransactions.filter {
                            _uiState.value.appliedTransactionsFilters.appliedTypeFilters.isEmptyOrContains(
                                it.toTransactionTypeFilterLabel()
                            )
                                    && meetsDateFilterConstraint(_uiState.value.appliedTransactionsFilters.appliedDateFilter, it.timestamp)
                        }
                    )
                }
            }
        }
    }

    private fun meetsDateFilterConstraint(
        dateFilter: FilterOption.Date?,
        timestamp: Instant
    ): Boolean {
        if (dateFilter == null) return true
        val today = LocalDate.now()

        val (startDate, endDate) = when(dateFilter) {
            is FilterOption.Date.CustomRange -> {
                dateFilter.startDate to dateFilter.endDate
            }
            FilterOption.Date.ThisMonth -> {
                today.withDayOfMonth(1) to today
            }
            FilterOption.Date.ThisWeek -> {
                today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)) to today
            }
        }
        return timestamp.toLocalDate() in startDate..endDate
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.TabSelect -> {
                viewModelScope.launch {
                    _uiEffect.send(
                        ScrollToTab(event.tabIndex)
                    )
                }
            }

            is HistoryEvent.SaveSelectedTab -> {
                _uiState.update { it.copy(selectedTab = it.tabs[event.tabIndex]) }
            }

            HistoryEvent.ApplyFilter -> {
                when(_uiState.value.selectedTab) {
                    HistoryTab.SESSIONS -> applySessionsFilter()
                    HistoryTab.TRANSACTIONS -> applyTransactionsFilter()
                }
            }

            HistoryEvent.ClearFilter -> {
                _uiState.update { state ->
                    when (state.selectedTab) {
                        HistoryTab.SESSIONS -> state.copy(
                            draftSessionsFilters = SessionsFilterUiState()
                        )

                        HistoryTab.TRANSACTIONS -> state.copy(
                            draftTransactionsFilters = TransactionsFilterUiState()
                        )
                    }
                }
            }

            HistoryEvent.FilterMenuIconClick -> {
                _uiState.update {
                    when(it.selectedTab) {
                        HistoryTab.SESSIONS -> it.copy(
                            draftSessionsFilters = it.appliedSessionsFilters.copy(
                                selectedFilterCategory = null
                            )
                        )
                        HistoryTab.TRANSACTIONS -> it.copy(
                            draftTransactionsFilters = it.appliedTransactionsFilters.copy(
                                selectedFilterCategory = null
                            )
                        )
                    }
                }
            }

            is HistoryEvent.FilterOptionSelected -> {
                handleFilterOptionSelection(event.option, event.isSelected)
            }

            is HistoryEvent.FilterCategorySelected -> {
                handleFilterMenuCategoryClick(event.category)
            }

            HistoryEvent.DismissFilterMenu -> {
                _uiState.update {
                    when(it.selectedTab) {
                        HistoryTab.SESSIONS -> it.copy(draftSessionsFilters = null)
                        HistoryTab.TRANSACTIONS -> it.copy(draftTransactionsFilters = null)
                    }
                }
            }

            is HistoryEvent.DaterFilterApplied -> {
                if (event.from == null || event.to == null) return

                val startDate = toLocalDate(event.from)
                val endDate = toLocalDate(event.to)
                val newDateFilter = FilterOption.Date.CustomRange(startDate, endDate)

                _uiState.update {
                    when(it.selectedTab) {
                        HistoryTab.SESSIONS -> it.copy(
                            showDatePickerDialog = false,
                            draftSessionsFilters = it.draftSessionsFilters?.copy(appliedDateFilter = newDateFilter)
                        )
                        HistoryTab.TRANSACTIONS -> it.copy(
                            showDatePickerDialog = false,
                            draftTransactionsFilters = it.draftTransactionsFilters?.copy(appliedDateFilter = newDateFilter)
                        )
                    }
                }
            }

            is HistoryEvent.DateRangePickerDismissed -> {
                _uiState.update {
                    it.copy(
                        showDatePickerDialog = false
                    )
                }
            }

            HistoryEvent.DateRangeFilterOptionClick -> {
                _uiState.update { it.copy(showDatePickerDialog = true) }
            }
        }
    }

    private fun applyTransactionsFilter() {
        _uiState.value.draftTransactionsFilters?.let { draftTransactionFilters ->
            _uiState.update { state ->
                state.copy(
                    filteredTransactions = state.allTransactions.filter {
                        draftTransactionFilters.appliedTypeFilters.isEmptyOrContains(it.toTransactionTypeFilterLabel())
                                && meetsDateFilterConstraint(draftTransactionFilters.appliedDateFilter, it.timestamp)
                    },
                    appliedTransactionsFilters = draftTransactionFilters,
                    draftTransactionsFilters = null
                )
            }
        }
    }

    private fun applySessionsFilter() {
        _uiState.value.draftSessionsFilters?.let { draftSessionsFilters ->
            _uiState.update { state ->
                state.copy(
                    filteredSessions = state.allSessions.filter {
                        draftSessionsFilters.appliedCompletionStatusFilters.isEmptyOrContains(it.toCompletionStatusFilterLabel())
                                && draftSessionsFilters.appliedDurationTypeFilters.isEmptyOrContains(it.toDurationFilterLabel())
                                && meetsDateFilterConstraint(draftSessionsFilters.appliedDateFilter, it.startTime)
                    },
                    appliedSessionsFilters = draftSessionsFilters,
                    draftSessionsFilters = null,
                )
            }
        }
    }

    private fun handleFilterMenuCategoryClick(filterCategory: FilterCategory) {
        _uiState.update {
            when (it.selectedTab) {
                HistoryTab.SESSIONS -> {
                    it.copy(
                        draftSessionsFilters = it.draftSessionsFilters?.copy(
                            selectedFilterCategory = filterCategory
                        )
                    )
                }

                HistoryTab.TRANSACTIONS -> {
                    it.copy(
                        draftTransactionsFilters = it.draftTransactionsFilters?.copy(
                            selectedFilterCategory = filterCategory
                        )
                    )
                }
            }
        }
    }

    private fun handleFilterOptionSelection(filterOption: FilterOption, isSelected: Boolean) {
        when (filterOption) {
            is FilterOption.SessionFilter.CompletionStatus.Cancelled, is FilterOption.SessionFilter.CompletionStatus.Finished -> {
                _uiState.update {
                    it.copy(
                        draftSessionsFilters = it.draftSessionsFilters?.copy(
                            appliedCompletionStatusFilters = it.draftSessionsFilters.appliedCompletionStatusFilters.updateSet(
                                filterOption,
                                isSelected
                            )
                        )
                    )
                }
            }

            is FilterOption.SessionFilter.DurationType.LongType, is FilterOption.SessionFilter.DurationType.ShortType -> {
                _uiState.update {
                    it.copy(
                        draftSessionsFilters = it.draftSessionsFilters?.copy(
                            appliedDurationTypeFilters = it.draftSessionsFilters.appliedDurationTypeFilters.updateSet(
                                filterOption,
                                isSelected
                            )
                        )
                    )
                }
            }

            is FilterOption.TransactionFilter.Type.TaskCredit, is FilterOption.TransactionFilter.Type.Redemption, is FilterOption.TransactionFilter.Type.Bonus -> {
                _uiState.update {
                    it.copy(
                        draftTransactionsFilters = it.draftTransactionsFilters?.copy(
                            appliedTypeFilters = it.draftTransactionsFilters.appliedTypeFilters.updateSet(
                                filterOption,
                                isSelected
                            )
                        )
                    )
                }
            }
            is FilterOption.Date.ThisWeek, is FilterOption.Date.ThisMonth  -> {
                when(_uiState.value.selectedTab) {
                    HistoryTab.SESSIONS -> {
                        _uiState.update {
                            it.copy(
                                draftSessionsFilters = it.draftSessionsFilters?.copy(
                                    appliedDateFilter = filterOption
                                )
                            )
                        }
                    }
                    HistoryTab.TRANSACTIONS -> {
                        _uiState.update {
                            it.copy(
                                draftTransactionsFilters = it.draftTransactionsFilters?.copy(
                                    appliedDateFilter = filterOption
                                )
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}