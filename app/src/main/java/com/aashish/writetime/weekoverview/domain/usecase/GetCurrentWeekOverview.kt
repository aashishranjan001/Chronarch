package com.aashish.writetime.weekoverview.domain.usecase

import com.aashish.writetime.weekoverview.domain.model.WeeklyOverview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetCurrentWeekOverview @Inject constructor(
    private val getCurrentWeekEarningsOverview: GetCurrentWeekEarningsOverview,
    private val getCurrentWeekSessionsOverview: GetCurrentWeekSessionsOverview
) {

    suspend operator fun invoke(): Flow<WeeklyOverview> {
        return combine(
            getCurrentWeekEarningsOverview(),
            getCurrentWeekSessionsOverview()
        ) { earnings, sessions ->
            WeeklyOverview(earnings, sessions)
        }
    }
}