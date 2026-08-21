package com.aashish.writetime.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.writetime.history.domain.usecase.GetHistoryUseCase
import com.aashish.writetime.history.presentation.model.HistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCase: GetHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            historyUseCase().collectLatest { history ->
                _uiState.update {
                    it.copy(transactions = history.transactions, sessions = history.sessions)
                }
            }
        }
    }

    fun onEvent(event: HistoryEvent) {
        when(event) {
            is HistoryEvent.TabSelect -> {
                _uiState.update {
                    it.copy(
                        selectedTabIndex = event.selectedTabIndex
                    )
                }
            }
        }
    }


}