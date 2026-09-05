package com.aashish.chronarch.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.chronarch.common.ui.isEmptyOrContains
import com.aashish.chronarch.common.ui.toLocalDate
import com.aashish.chronarch.common.ui.updateSet
import com.aashish.chronarch.history.domain.usecase.GetHistoryUseCase
import com.aashish.chronarch.history.presentation.HistoryUiEffect.*
import com.aashish.chronarch.history.presentation.model.FilterCategory
import com.aashish.chronarch.history.presentation.model.FilterCategoryItemUiState
import com.aashish.chronarch.history.presentation.model.filterOption
import com.aashish.chronarch.history.presentation.model.filterOption.Date.*
import com.aashish.chronarch.history.presentation.model.FilterOptionItem
import com.aashish.chronarch.history.presentation.model.HistoryTab
import com.aashish.chronarch.history.presentation.model.HistoryUiState
import com.aashish.chronarch.history.presentation.model.SessionsFilters
import com.aashish.chronarch.history.presentation.model.TransactionsFilters
import com.aashish.chronarch.history.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCase: GetHistoryUseCase
) : ViewModel() {

    private val dataFetchTrigger = MutableSharedFlow<Unit>()
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
            dataFetchTrigger.onStart {
                    emit(Unit)
                }.flatMapLatest {
                    historyUseCase()
                }.onStart {
                    _uiState.update {
                        it.copy(isLoading = true, isError = false)
                    }
                }.catch {
                    _uiState.update {
                        it.copy(isLoading = false, isError = true)
                    }
                }.collectLatest { history ->
                    val allTransactions =
                        history.transactions.map { transaction -> transaction.toUiModel() }
                    val allSessions = history.sessions.map { session -> session.toUiModel() }
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isError = false,
                            allTransactions = allTransactions,
                            allSessions = allSessions,
                            filteredSessions = allSessions.filter {
                                _uiState.value.appliedSessionsFilters.appliedCompletionStatusFilters.isEmptyOrContains(
                                    it.toCompletionStatusFilterLabel()
                                ) && state.appliedSessionsFilters.appliedDurationTypeFilters.isEmptyOrContains(
                                    it.toDurationFilterLabel()
                                ) && meetsDateFilterConstraint(
                                    state.appliedSessionsFilters.appliedDateFilter, it.startTime
                                )
                            },
                            filteredTransactions = allTransactions.filter {
                                state.appliedTransactionsFilters.appliedTypeFilters.isEmptyOrContains(
                                    it.toTransactionTypeFilterLabel()
                                ) && meetsDateFilterConstraint(
                                    state.appliedTransactionsFilters.appliedDateFilter, it.timestamp
                                )
                            })
                    }
                }
        }
    }

    private fun meetsDateFilterConstraint(
        dateFilter: filterOption.Date?,
        timestamp: Instant
    ): Boolean {
        if (dateFilter == null) return true
        val today = LocalDate.now()

        val (startDate, endDate) = when(dateFilter) {
            is CustomRange -> {
                dateFilter.startDate to dateFilter.endDate
            }
            ThisMonth -> {
                today.withDayOfMonth(1) to today
            }
            ThisWeek -> {
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

            HistoryEvent.ApplyDraftFilters -> {
                when(_uiState.value.selectedTab) {
                    HistoryTab.SESSIONS -> applySessionsFilter(_uiState.value.draftSessionsFilters)
                    HistoryTab.TRANSACTIONS -> applyTransactionsFilter(_uiState.value.draftTransactionsFilters)
                }
            }

            HistoryEvent.ResetAppliedFilters -> {
                when (_uiState.value.selectedTab) {
                    HistoryTab.SESSIONS -> applySessionsFilter(SessionsFilters())
                    HistoryTab.TRANSACTIONS -> applyTransactionsFilter(TransactionsFilters())
                }
            }

            HistoryEvent.ClearDraftFilters -> {
                _uiState.update { state ->
                    when (state.selectedTab) {
                        HistoryTab.SESSIONS -> state.copy(
                            draftSessionsFilters = SessionsFilters(),
                            filterCategories = getInitialSessionFilterCategoriesList(SessionsFilters()),
                            filterOptions = emptyList(),
                        )

                        HistoryTab.TRANSACTIONS -> state.copy(
                            draftTransactionsFilters = TransactionsFilters(),
                            filterCategories = getInitialTransactionFilterCategoriesList(TransactionsFilters()),
                            filterOptions = emptyList()
                        )
                    }
                }
            }

            HistoryEvent.FilterMenuIconClick -> {
                populateBottomSheetInitialData()
            }

            is HistoryEvent.FilterCheckboxOptionSelected -> {
                handleCheckboxFilterOptionSelection(event.optionItem, event.isSelected)
            }
            is HistoryEvent.FilterRadioOptionSelected -> {
                handleRadioFilterOptionClick(event.optionItem)
            }

            is HistoryEvent.FilterCategorySelected -> {
                handleFilterMenuCategoryClick(event.category)
            }

            HistoryEvent.DismissFilterMenu -> {
                _uiState.update {
                    when(it.selectedTab) {
                        HistoryTab.SESSIONS -> it.copy(draftSessionsFilters = null, filterCategories = emptyList(), filterOptions = emptyList())
                        HistoryTab.TRANSACTIONS -> it.copy(draftTransactionsFilters = null, filterCategories = emptyList(), filterOptions = emptyList())
                    }
                }
            }

            is HistoryEvent.DaterFilterApplied -> {
                if (event.from == null || event.to == null) return

                val startDate = toLocalDate(event.from)
                val endDate = toLocalDate(event.to)
                val newDateFilter = CustomRange(startDate, endDate)

                val updatedFilterOptions = _uiState.value.filterOptions.map { optionType ->
                    when (optionType) {
                        is FilterOptionItem.DateRange -> { // in case of multiple filterOptions of type DateRange, compare the picker launching option id with optionType's id
                            optionType.copy(startDate = startDate, endDate = endDate)
                        }

                        is FilterOptionItem.Radio -> {
                            optionType.copy(isSelected = false)
                        }

                        else -> {
                            optionType
                        }
                    }
                }

                _uiState.update {
                    when(it.selectedTab) {
                        HistoryTab.SESSIONS -> it.copy(
                            showDatePickerDialog = false,
                            draftSessionsFilters = it.draftSessionsFilters?.copy(appliedDateFilter = newDateFilter),
                            filterOptions = updatedFilterOptions
                        )
                        HistoryTab.TRANSACTIONS -> it.copy(
                            showDatePickerDialog = false,
                            draftTransactionsFilters = it.draftTransactionsFilters?.copy(appliedDateFilter = newDateFilter),
                            filterOptions = updatedFilterOptions
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

            HistoryEvent.RetryClick -> {
                dataFetchTrigger.tryEmit(Unit)
            }
        }
    }

    private fun getInitialSessionFilterCategoriesList(sessionFilter: SessionsFilters): List<FilterCategoryItemUiState> {
        return listOf(
            FilterCategoryItemUiState(
                category = FilterCategory.SessionFilter.CompletionStatus,
                hasAnyFilterOptionSelected = sessionFilter.appliedCompletionStatusFilters.isNotEmpty(),
            ),
            FilterCategoryItemUiState(
                category = FilterCategory.SessionFilter.DurationType,
                hasAnyFilterOptionSelected = sessionFilter.appliedDurationTypeFilters.isNotEmpty(),
            ),
            FilterCategoryItemUiState(
                category = FilterCategory.Date,
                hasAnyFilterOptionSelected = (sessionFilter.appliedDateFilter != null)
            )
        )
    }

    private fun getInitialTransactionFilterCategoriesList(transactionFilters: TransactionsFilters): List<FilterCategoryItemUiState> {
        return listOf(
            FilterCategoryItemUiState(
                category = FilterCategory.TransactionFilter.Type,
                hasAnyFilterOptionSelected = transactionFilters.appliedTypeFilters.isNotEmpty(),
            ),
            FilterCategoryItemUiState(
                category = FilterCategory.Date,
                hasAnyFilterOptionSelected = (transactionFilters.appliedDateFilter != null)
            )
        )
    }

    private fun populateBottomSheetInitialData() {
            val state = _uiState.value
            val filterCategories = when(state.selectedTab) {
                HistoryTab.SESSIONS -> {
                    getInitialSessionFilterCategoriesList(state.appliedSessionsFilters)
                }
                HistoryTab.TRANSACTIONS -> {
                    getInitialTransactionFilterCategoriesList(state.appliedTransactionsFilters)
                }
            }

            _uiState.update {
                when(it.selectedTab) {
                    HistoryTab.SESSIONS -> {
                        it.copy(
                            draftSessionsFilters = it.appliedSessionsFilters,
                            filterCategories = filterCategories,
                        )
                    }
                    HistoryTab.TRANSACTIONS -> {
                        it.copy(
                            draftTransactionsFilters = it.appliedTransactionsFilters,
                            filterCategories = filterCategories
                        )
                    }
                }
            }

    }

    private fun applyTransactionsFilter(filters: TransactionsFilters?) {
        filters?.let { transactionFilters ->
            _uiState.update { state ->
                state.copy(
                    filteredTransactions = state.allTransactions.filter {
                        transactionFilters.appliedTypeFilters.isEmptyOrContains(it.toTransactionTypeFilterLabel())
                                && meetsDateFilterConstraint(transactionFilters.appliedDateFilter, it.timestamp)
                    },
                    appliedTransactionsFilters = transactionFilters,
                    draftTransactionsFilters = null,
                    filterCategories = emptyList(),
                    filterOptions = emptyList(),
                )
            }
        }
    }

    private fun applySessionsFilter(filters: SessionsFilters?) {
        filters?.let { sessionFilters ->
            _uiState.update { state ->
                state.copy(
                    filteredSessions = state.allSessions.filter {
                        sessionFilters.appliedCompletionStatusFilters.isEmptyOrContains(it.toCompletionStatusFilterLabel())
                                && sessionFilters.appliedDurationTypeFilters.isEmptyOrContains(it.toDurationFilterLabel())
                                && meetsDateFilterConstraint(sessionFilters.appliedDateFilter, it.startTime)
                    },
                    appliedSessionsFilters = sessionFilters,
                    draftSessionsFilters = null,
                    filterCategories = emptyList(),
                    filterOptions = emptyList(),
                )
            }
        }
    }

    private fun handleFilterMenuCategoryClick(filterCategory: FilterCategory) {

        val updatedFilterCategories = _uiState.value.filterCategories.map { categoryState ->
            categoryState.copy(showSelected = categoryState.category == filterCategory)
        }

        val updatedOptionsList = buildList {
            _uiState.value.draftSessionsFilters?.let { sessionFilters ->
                when (filterCategory) {
                    FilterCategory.Date -> {
                        add(
                            FilterOptionItem.Radio(ThisWeek, sessionFilters.appliedDateFilter is ThisWeek)
                        )
                        add(
                            FilterOptionItem.Radio(ThisMonth, sessionFilters.appliedDateFilter is ThisMonth)
                        )
                        (sessionFilters.appliedDateFilter as? CustomRange).let { dateRange ->
                            add(
                                FilterOptionItem.DateRange(dateRange?.startDate, dateRange?.endDate)
                            )
                        }
                    }
                    FilterCategory.SessionFilter.CompletionStatus -> {
                        filterOption.SessionFilter.CompletionStatus.entries.forEach { completionStatusFilterOption ->
                            add(
                                FilterOptionItem.Checkbox(completionStatusFilterOption, sessionFilters.appliedCompletionStatusFilters.contains(completionStatusFilterOption))
                            )
                        }
                    }
                    FilterCategory.SessionFilter.DurationType -> {
                        filterOption.SessionFilter.DurationType.entries.forEach { durationTypeFilterOption ->
                            add(
                                FilterOptionItem.Checkbox(durationTypeFilterOption, sessionFilters.appliedDurationTypeFilters.contains(durationTypeFilterOption))
                            )
                        }
                    }
                    else -> {}
                }
            }
            uiState.value.draftTransactionsFilters?.let { transactionsFilter ->
                when(filterCategory) {
                    FilterCategory.TransactionFilter.Type -> {
                        filterOption.TransactionFilter.Type.entries.forEach { transactionTypeFilterOption ->
                            add(
                                FilterOptionItem.Checkbox(transactionTypeFilterOption, transactionsFilter.appliedTypeFilters.contains(transactionTypeFilterOption))
                            )
                        }
                    }
                    FilterCategory.Date -> {
                        add(
                            FilterOptionItem.Radio(ThisWeek, transactionsFilter.appliedDateFilter is ThisWeek)
                        )
                        add(
                            FilterOptionItem.Radio(ThisMonth, transactionsFilter.appliedDateFilter is ThisMonth)
                        )
                        (transactionsFilter.appliedDateFilter as? CustomRange).let { dateRange ->
                            add(
                                FilterOptionItem.DateRange(dateRange?.startDate, dateRange?.endDate)
                            )
                        }
                    }
                    else -> {}
                }
            }
        }

        _uiState.update {
            it.copy(
                filterCategories = updatedFilterCategories,
                filterOptions = updatedOptionsList
            )
        }
    }

    private fun handleRadioFilterOptionClick(optionItem: FilterOptionItem.Radio) {
        val updatedFilterOptions = _uiState.value.filterOptions.map { item ->
            when(item) {
                is FilterOptionItem.Radio -> item.copy(isSelected = optionItem.option == item.option)
                is FilterOptionItem.DateRange -> item.copy(startDate = null, endDate = null)
                else -> item
            }
        }
        when(optionItem.option) {
            is ThisWeek, is ThisMonth  -> {
                when(_uiState.value.selectedTab) {
                    HistoryTab.SESSIONS -> {
                        _uiState.update {
                            it.copy(
                                draftSessionsFilters = it.draftSessionsFilters?.copy(
                                    appliedDateFilter = optionItem.option
                                ),
                                filterOptions = updatedFilterOptions
                            )
                        }
                    }
                    HistoryTab.TRANSACTIONS -> {
                        _uiState.update {
                            it.copy(
                                draftTransactionsFilters = it.draftTransactionsFilters?.copy(
                                    appliedDateFilter = optionItem.option
                                ),
                                filterOptions = updatedFilterOptions
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }

    private fun handleCheckboxFilterOptionSelection(optionItem: FilterOptionItem.Checkbox, isSelected: Boolean) {
        val updatedFilterOptions = _uiState.value.filterOptions.map { item ->
            when(item) {
                is FilterOptionItem.Checkbox -> if (item.option == optionItem.option) item.copy(isSelected = isSelected) else item
                else -> item
            }
        }
        when (optionItem.option) {
            is filterOption.SessionFilter.CompletionStatus.Cancelled, is filterOption.SessionFilter.CompletionStatus.Finished -> {
                _uiState.update {
                    it.copy(
                        draftSessionsFilters = it.draftSessionsFilters?.copy(
                            appliedCompletionStatusFilters = it.draftSessionsFilters.appliedCompletionStatusFilters.updateSet(
                                optionItem.option,
                                isSelected
                            )
                        ),
                        filterOptions = updatedFilterOptions
                    )
                }
            }

            is filterOption.SessionFilter.DurationType.LongType, is filterOption.SessionFilter.DurationType.ShortType -> {
                _uiState.update {
                    it.copy(
                        draftSessionsFilters = it.draftSessionsFilters?.copy(
                            appliedDurationTypeFilters = it.draftSessionsFilters.appliedDurationTypeFilters.updateSet(
                                optionItem.option,
                                isSelected
                            )
                        ),
                        filterOptions = updatedFilterOptions
                    )
                }
            }

            is filterOption.TransactionFilter.Type.TaskCredit, is filterOption.TransactionFilter.Type.Redemption, is filterOption.TransactionFilter.Type.Bonus -> {
                _uiState.update {
                    it.copy(
                        draftTransactionsFilters = it.draftTransactionsFilters?.copy(
                            appliedTypeFilters = it.draftTransactionsFilters.appliedTypeFilters.updateSet(
                                optionItem.option,
                                isSelected
                            )
                        ),
                        filterOptions = updatedFilterOptions
                    )
                }
            }
            else -> {}
        }
    }
}