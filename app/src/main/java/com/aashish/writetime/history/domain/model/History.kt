package com.aashish.writetime.history.domain.model

import com.aashish.writetime.common.domain.model.FocusPointTransaction

data class History(
    val transactions: List<FocusPointTransaction>,
    val sessions: List<EndedTimerSession>
)