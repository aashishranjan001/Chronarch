package com.aashish.writetime.weekoverview.domain.usecase

import com.aashish.writetime.common.domain.model.EarningOverview
import com.aashish.writetime.common.domain.usecase.CalculateDailyEarningUseCase
import com.aashish.writetime.weekoverview.domain.model.WeeklyEarningOverview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetCurrentWeekEarningsOverview @Inject constructor(
    private val getDailyEarningUseCase: CalculateDailyEarningUseCase,
) {
    suspend operator fun invoke(): Flow<WeeklyEarningOverview> {
        val today = LocalDate.now()
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val dayWiseEarningMap = mutableMapOf<DayOfWeek, EarningOverview?>()
        for (i in 0L..6L) {
            val date = startOfWeek.plusDays(i)
            dayWiseEarningMap[date.dayOfWeek] = if (date < today) {
                getDailyEarningUseCase(date).first()
            } else null
        }

        return getDailyEarningUseCase(today).map { currentDateEarningOverview ->
            dayWiseEarningMap[today.dayOfWeek] = currentDateEarningOverview

            WeeklyEarningOverview(
                totalEarningOverview = EarningOverview(
                    completionFocusPoints = dayWiseEarningMap.values.filterNotNull().sumOf { it.completionFocusPoints },
                    bonusFocusPoints = dayWiseEarningMap.values.filterNotNull().sumOf { it.bonusFocusPoints },
                    redeemedFocusPoints = dayWiseEarningMap.values.filterNotNull().sumOf { it.redeemedFocusPoints }
                ),
                dayWiseEarningOverview = dayWiseEarningMap
            )
        }
    }
}