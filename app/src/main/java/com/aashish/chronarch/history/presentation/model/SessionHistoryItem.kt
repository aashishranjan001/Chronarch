package com.aashish.chronarch.history.presentation.model

import com.aashish.chronarch.common.domain.model.DurationType
import com.aashish.chronarch.common.ui.toLocalDate
import com.aashish.chronarch.history.domain.model.EndedTimerSession
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

    fun toDurationFilterLabel(): filterOption.SessionFilter.DurationType {
        return when (durationType) {
            DurationType.LongDuration -> filterOption.SessionFilter.DurationType.LongType
            DurationType.ShortDuration -> filterOption.SessionFilter.DurationType.ShortType
        }
    }

    fun toCompletionStatusFilterLabel(): filterOption.SessionFilter.CompletionStatus {
        return if (runDuration >= durationType.duration) filterOption.SessionFilter.CompletionStatus.Finished // should ideally be runDuration == durationType.duraiton
        else filterOption.SessionFilter.CompletionStatus.Cancelled
    }
}

fun EndedTimerSession.toUiModel() = SessionHistoryItem(
    id = id,
    startTime = startTime,
    endTime = endTime,
    durationType = durationType,
    runDuration = runDuration
)