package com.aashish.writetime.history.presentation.model

import androidx.annotation.StringRes
import com.aashish.writetime.R

enum class HistoryTabItem(@StringRes val titleRes: Int) {
    SESSIONS(R.string.sessions),
    TRANSACTION(R.string.transactions)
}