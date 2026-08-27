package com.aashish.chronarch.redemption.presentation.redemption_corner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.chronarch.redemption.domain.usecase.GetRedemptionCornerOverviewUseCase
import com.aashish.chronarch.redemption.domain.usecase.RedeemRewardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RedemptionCornerViewModel @Inject constructor(
    private val getRedemptionCornerOverviewUseCase: GetRedemptionCornerOverviewUseCase,
    private val redeemRewardsUseCase: RedeemRewardUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(RedemptionCornerUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<RedemptionCornerUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private val dataFetchTrigger = MutableSharedFlow<Unit>()

    init {
        viewModelScope.launch {
            dataFetchTrigger.onStart {
                emit(Unit)
            }.flatMapLatest {
                    getRedemptionCornerOverviewUseCase()
                }.onStart {
                    _uiState.update {
                        it.copy(isLoading = true, isError = false)
                    }
                }.catch {
                    _uiState.update {
                        it.copy(isLoading = false, isError = true)
                    }
                }.collectLatest { overviewData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            rewardsList = overviewData.rewards,
                            availableFocusPointsBalance = overviewData.availableBalance
                        )
                    }
                }
        }
    }

    fun onEvent(event: RedemptionCornerEvent) {
        when (event) {
            RedemptionCornerEvent.DismissRewardsRedeemDialog -> {
                _uiState.update {
                    it.copy(selectedRedeemableReward = null)
                }
            }
            is RedemptionCornerEvent.RewardActionClick -> {
                _uiState.update {
                    it.copy(
                        selectedRedeemableReward = event.reward
                    )
                }
            }
            is RedemptionCornerEvent.RewardsRedeemDialogOptionClick -> {
                if (event.confirmed) {
                    _uiState.value.selectedRedeemableReward?.let {
                        viewModelScope.launch {
                            redeemRewardsUseCase(it.cost, it.name)
                            _uiEffect.send(
                                RedemptionCornerUiEffect.ShowRewardsRedeemedMessage(it.name)
                            )
                        }
                    }
                }
                _uiState.update {
                    it.copy(selectedRedeemableReward = null)
                }
            }
            RedemptionCornerEvent.SetupRewardActions -> {
                viewModelScope.launch {
                    _uiEffect.send(
                        RedemptionCornerUiEffect.LaunchRewardsSetup
                    )
                }
            }

            RedemptionCornerEvent.RetryClick -> {
                dataFetchTrigger.tryEmit(Unit)
            }
        }
    }
}