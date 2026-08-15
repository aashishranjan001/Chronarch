package com.aashish.writetime.common.domain.model

import kotlin.time.Instant

data class FocusPointTransaction(
    val id: Long,
    val value: Int,
    val transactionType: FocusPointTransactionType,
    val timestamp: Instant,
    val message: String
)