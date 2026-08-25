package com.aashish.writetime.history.presentation.model

import com.aashish.writetime.common.domain.model.DurationType
import com.aashish.writetime.common.ui.toLocalDate
import com.aashish.writetime.history.domain.model.EndedTimerSession
import java.time.Instant
import kotlin.time.Duration

data class SessionHistoryItem(
    val id: Long,
    val startTime: Instant,
    val endTime: Instant,
    val durationType: DurationType,
    val runDuration: Duration
) {

    val date
        get() = startTime.toLocalDate()

    fun toDurationFilterLabel(): FilterOption.SessionFilter.DurationType {
        return when (durationType) {
            DurationType.LongDuration -> FilterOption.SessionFilter.DurationType.LongType
            DurationType.ShortDuration -> FilterOption.SessionFilter.DurationType.ShortType
        }
    }

    fun toCompletionStatusFilterLabel(): FilterOption.SessionFilter.CompletionStatus {
        return if (runDuration >= durationType.duration) FilterOption.SessionFilter.CompletionStatus.Finished
        else FilterOption.SessionFilter.CompletionStatus.Cancelled
    }
}

fun EndedTimerSession.toUiModel() = SessionHistoryItem(
    id = id,
    startTime = startTime,
    endTime = endTime,
    durationType = durationType,
    runDuration = runDuration
)