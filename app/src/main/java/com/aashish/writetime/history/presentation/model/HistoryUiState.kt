package com.aashish.writetime.history.presentation.model

import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.history.domain.model.EndedTimerSession

data class HistoryUiState(
    val tabs: List<HistoryTab>,
    val transactions: List<FocusPointTransaction> = emptyList(),
    val sessions: List<EndedTimerSession> = emptyList()
)