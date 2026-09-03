package com.aashish.chronarch.home.domain.model

import android.os.Parcelable
import com.aashish.chronarch.common.domain.model.DurationType
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class LatestTimerSession(
    val id: Long,
    val durationType: DurationType,
    val idealEndTime: Instant,
    val sessionEndTime: Instant?,
    val runningStreakProgressionFraction: Double
): Parcelable