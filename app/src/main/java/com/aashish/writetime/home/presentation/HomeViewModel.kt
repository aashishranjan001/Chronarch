package com.aashish.writetime.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.writetime.common.domain.model.DurationType
import com.aashish.writetime.common.domain.usecase.CalculateDailyEarningUseCase
import com.aashish.writetime.common.ui.UiEffect
import com.aashish.writetime.home.domain.usecase.AddNewTimerSessionUseCase
import com.aashish.writetime.home.domain.usecase.GetCurrentDaySessionsOverview
import com.aashish.writetime.home.domain.usecase.EndTimerSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    getCurrentDateEarningUseCase: CalculateDailyEarningUseCase,
    getCurrentDaySessionsOverview: GetCurrentDaySessionsOverview,
    private val endTimerSessionUseCase: EndTimerSessionUseCase,
    private val addNewTimerSessionUseCase: AddNewTimerSessionUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {

        viewModelScope.launch {
            getCurrentDateEarningUseCase(LocalDate.now()).collectLatest { earningOverview ->
                _uiState.update {
                    it.copy(
                        currentDateCreditFocusPoints = earningOverview.creditFocusPoints,
                        currentDateDebitFocusPoints = earningOverview.redeemedFocusPoints
                    )
                }
            }
        }

        viewModelScope.launch {
            getCurrentDaySessionsOverview().collectLatest { sessionsOverview ->
                _uiState.update {
                    it.copy(
                        totalSessionsStarted = sessionsOverview.totalStartedCount,
                        totalSessionsCompleted = sessionsOverview.successfulCompletionCount,
                        totalSessionsCancelled = sessionsOverview.cancelledCount,
                        streaksCount = sessionsOverview.totalStreaks,
                        activeTimer = sessionsOverview.activeSession?.let { activeSessionInfo ->
                            ActiveTimer(
                                sessionId = activeSessionInfo.id,
                                durationRemainingInSeconds = activeSessionInfo.durationRemainingSeconds,
                                duration = activeSessionInfo.durationType.duration
                            )
                        },
                        streakProgressFraction = sessionsOverview.currentStreakProgressionFraction
                    )
                }

                sessionsOverview.activeSession?.durationRemainingSeconds?.let { remainingDuration ->
                    for (i in remainingDuration downTo  0) {
                        _uiState.update {
                            it.copy(
                                activeTimer = it.activeTimer?.copy(durationRemainingInSeconds = i)
                            )
                        }
                        delay(1.seconds)
                    }
                    endTimerSessionUseCase(
                        sessionId = sessionsOverview.activeSession.id,
                        idealCompletionTime = Instant.now(),
                        durationType = sessionsOverview.activeSession.durationType,
                        streakProgressFraction = _uiState.value.streakProgressFraction
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.CancelTimer -> {
                viewModelScope.launch {
                    _uiState.value.activeTimer?.let {
                        endTimerSessionUseCase(
                            sessionId = it.sessionId,
                            idealCompletionTime = Instant.now().plusSeconds(it.durationRemainingInSeconds),
                            durationType = DurationType.fromDuration(it.duration.inWholeMilliseconds),
                            streakProgressFraction = _uiState.value.streakProgressFraction

                        )
                    }
                }
            }
            HomeEvent.StartTimer -> {
                viewModelScope.launch {
                    addNewTimerSessionUseCase(
                        durationType = _uiState.value.selectedNewTimerDurationType,
                        runningStreakProgressFraction = _uiState.value.streakProgressFraction,
                    )
                }
            }

            is HomeEvent.SelectNewTimerDurationType -> {
                _uiState.update {
                    it.copy(
                        selectedNewTimerDurationType = event.durationType
                    )
                }
            }
        }
    }
}