package com.aashish.writetime.common.data.local.mappers

import com.aashish.writetime.common.data.local.model.FocusPointsTransactionEntity
import com.aashish.writetime.common.data.local.model.TimerSessionEntity
import com.aashish.writetime.common.domain.model.DurationType
import com.aashish.writetime.common.domain.model.FocusPointTransaction
import com.aashish.writetime.common.domain.model.TimerSession
import java.time.Instant

fun TimerSession.toEntity(): TimerSessionEntity = TimerSessionEntity(
    id = id,
    targetDurationMillis = durationType.duration.inWholeMilliseconds,
    startedAt = startTime.toEpochMilli(),
    completedAt = endTime?.toEpochMilli(),
    streakProgressFraction = streakProgressFraction
)

fun TimerSessionEntity.toDomain(): TimerSession = TimerSession(
    id = id,
    durationType = DurationType.fromDuration(targetDurationMillis),
    startTime = Instant.ofEpochMilli(startedAt),
    endTime = completedAt?.let { Instant.ofEpochMilli(it) },
    streakProgressFraction = streakProgressFraction
)

fun FocusPointTransaction.toEntity(): FocusPointsTransactionEntity = FocusPointsTransactionEntity(
    transactionType = transactionType,
    focusPoints = value,
    timestampMillis = timestamp.toEpochMilli(),
    message = message
)

fun FocusPointsTransactionEntity.toDomain(): FocusPointTransaction = FocusPointTransaction(
    id = id,
    value = focusPoints,
    transactionType = transactionType,
    timestamp = Instant.ofEpochMilli(timestampMillis),
    message = message
)
