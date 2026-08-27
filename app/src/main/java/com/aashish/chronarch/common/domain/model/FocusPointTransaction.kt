package com.aashish.chronarch.common.domain.model

import java.time.Instant

data class FocusPointTransaction(
    val id: Long,
    val value: Int,
    val transactionType: FocusPointTransactionType,
    val timestamp: Instant,
    val message: String
)