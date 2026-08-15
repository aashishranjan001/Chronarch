package com.aashish.writetime.common.data.local.mappers

import com.aashish.writetime.common.data.local.model.FocusPointsTransactionEntity
import com.aashish.writetime.common.data.local.model.TimerSessionEntity
import com.aashish.writetime.common.domain.model.CompletionStatus
import com.aashish.writetime.common.domain.model.DurationType
import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.domain.model.TimerSession
import kotlin.time.Instant

fun TimerSession.toEntity(): TimerSessionEntity = TimerSessionEntity(
    id = id,
    targetDurationMillis = durationType.duration.inWholeMilliseconds,
    startedAt = startTime.toEpochMilliseconds(),
    completedAt = endTime?.toEpochMilliseconds(),
    streakProgressFraction = streakProgressFraction
)

fun TimerSessionEntity.toDomain(): TimerSession = TimerSession(
    id = id,
    durationType = DurationType.fromDuration(targetDurationMillis),
    startTime = Instant.fromEpochMilliseconds(startedAt),
    endTime = completedAt?.let { Instant.fromEpochMilliseconds(it) },
    completionStatus = if (completedAt != null) CompletionStatus.COMPLETED else CompletionStatus.NOT_COMPLETED,
    streakProgressFraction = streakProgressFraction
)

fun FocusPointTransaction.toEntity(): FocusPointsTransactionEntity = FocusPointsTransactionEntity(
    transactionType = transactionType,
    focusPoints = value,
    timestampMillis = timestamp.toEpochMilliseconds(),
    message = message
)

fun FocusPointsTransactionEntity.toDomain(): FocusPointTransaction = FocusPointTransaction(
    id = id,
    value = focusPoints,
    transactionType = transactionType,
    timestamp = Instant.fromEpochMilliseconds(timestampMillis),
    message = message
)
