package com.aashish.chronarch.history.domain.model

import com.aashish.chronarch.common.domain.model.FocusPointTransaction

data class History(
    val transactions: List<FocusPointTransaction>,
    val sessions: List<EndedTimerSession>
)