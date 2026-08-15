package com.aashish.writetime.home.domain.usecase

import com.aashish.writetime.common.domain.usecase.CalculateDailyEarningUseCase
import com.aashish.writetime.home.domain.model.HomeOverview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class GetHomeOverviewUseCase @Inject constructor(
    private val getCurrentDateEarningUseCase: CalculateDailyEarningUseCase,
    private val getCurrentDaySessionsOverview: GetCurrentDaySessionsOverview
) {

    operator fun invoke(): Flow<HomeOverview> {
        return combine(
            getCurrentDateEarningUseCase(LocalDate.now()),
            getCurrentDaySessionsOverview()
        ) { earningOverview, sessionsOverview ->
            HomeOverview(
                earningOverview = earningOverview,
                sessionsOverview = sessionsOverview
            )
        }
    }
}