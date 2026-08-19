package com.aashish.writetime.redemption.presentation.redemption_corner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.writetime.redemption.domain.model.RedeemableReward
import com.aashish.writetime.redemption.domain.usecase.GetRedemptionCornerOverviewUseCase
import com.aashish.writetime.redemption.domain.usecase.RedeemRewardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RedemptionCornerViewModel @Inject constructor(
    private val getRedemptionCornerOverviewUseCase: GetRedemptionCornerOverviewUseCase,
    private val redeemRewardsUseCase: RedeemRewardUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(RedemptionCornerUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<RedemptionCornerUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            getRedemptionCornerOverviewUseCase().collectLatest { overviewData ->
                _uiState.update {
                    it.copy(
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
        }
    }
}