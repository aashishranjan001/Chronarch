package com.aashish.writetime.redemption.presentation.rewards_setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.writetime.redemption.domain.usecase.AddRewardsUseCase
import com.aashish.writetime.redemption.presentation.rewards_setup.RewardsSetupOverlayType.*
import com.aashish.writetime.redemption.presentation.rewards_setup.transformers.toDomainModel
import com.aashish.writetime.redemption.presentation.rewards_setup.transformers.toEditModeRewardItem
import com.aashish.writetime.redemption.presentation.rewards_setup.transformers.toRewardSetupItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus
import kotlin.uuid.Uuid

@HiltViewModel
class SetupRewardsViewModel @Inject constructor(
    private val addRewardsUseCase: AddRewardsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupRewardsUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<SetupRewardsUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: SetupRewardsEvent) {
        when (event) {
            SetupRewardsEvent.BackPressed, SetupRewardsEvent.AbortSetupClick -> {
                _uiState.update {
                    it.copy(
                        overlay = AbortSetupDialog
                    )
                }
            }

            SetupRewardsEvent.AddActionRewardClick -> {
                _uiState.update {
                    it.copy(
                        overlay = AddRewardBottomSheet, currentEditingReward = EditModeRewardItem(
                            Uuid.random().toString(), "", ""
                        )
                    )
                }
            }

            is SetupRewardsEvent.DeleteActionRewardClick -> {
                _uiState.update {
                    it.copy(
                        overlay = DeleteRewardDialog(event.reward)
                    )
                }
            }

            SetupRewardsEvent.SaveChangesClick -> {
                _uiState.update {
                    it.copy(
                        overlay = FinishSetupDialog
                    )
                }
            }

            is SetupRewardsEvent.RewardItemClick -> {
                _uiState.update {
                    it.copy(
                        overlay = UpdateRewardBottomSheet,
                        currentEditingReward = event.reward.toEditModeRewardItem()
                    )
                }
            }

            is SetupRewardsEvent.CurrentRewardCostChanged -> {
                _uiState.update {
                    it.copy(
                        currentEditingReward = it.currentEditingReward?.copy(cost = event.cost)
                    )
                }
            }

            is SetupRewardsEvent.CurrentRewardNameChanged -> {
                _uiState.update {
                    it.copy(
                        currentEditingReward = it.currentEditingReward?.copy(name = event.name)
                    )
                }
            }

            is SetupRewardsEvent.OverlayDismissed -> {
                _uiState.update {
                    it.copy(
                        overlay = null,
                        currentEditingReward = it.currentEditingReward.takeIf { event.overlayType != AddRewardBottomSheet && event.overlayType != UpdateRewardBottomSheet },
                    )
                }
            }

            is SetupRewardsEvent.OverlayConfirmClicked -> {
                handleOverlayConfirmClicked(event.overlayType)
            }
        }
    }

    private fun handleOverlayConfirmClicked(overlayType: RewardsSetupOverlayType) {
        when (overlayType) {

            AddRewardBottomSheet -> addActionReward()

            UpdateRewardBottomSheet -> updateActionReward()

            AbortSetupDialog -> {
                _uiState.update { it.copy(overlay = null) }
                viewModelScope.launch { _uiEffect.send(SetupRewardsUiEffect.Finish) }
            }

            is DeleteRewardDialog -> {
                _uiState.update { it.copy(
                    overlay = null,
                    addedRewardItems = it.addedRewardItems.mapNotNull {listItem -> if (listItem.id == overlayType.reward.id) null else listItem }
                ) }
                viewModelScope.launch { _uiEffect.send(SetupRewardsUiEffect.RewardDeletedSnackbar) }
            }

            FinishSetupDialog -> {
                viewModelScope.launch {
                    addRewardsUseCase(_uiState.value.addedRewardItems.map { it.toDomainModel() })
                    _uiState.update {
                        it.copy(
                            overlay = null,
                        )
                    }
                    _uiEffect.send(SetupRewardsUiEffect.Finish)
                }
            }
        }
    }

    private fun addActionReward() {
        val transformedRewardItem = _uiState.value.currentEditingReward?.toRewardSetupItem()

        viewModelScope.launch {
            if (transformedRewardItem != null) {
                _uiState.update {
                    it.copy(
                        addedRewardItems = it.addedRewardItems + transformedRewardItem,
                        currentEditingReward = null,
                        overlay = null
                    )
                }
                _uiEffect.send(SetupRewardsUiEffect.RewardAddedSnackbar(true))
            } else {
                _uiState.update {
                    it.copy(
                        overlay = null
                    )
                }
                _uiEffect.send(SetupRewardsUiEffect.RewardAddedSnackbar(false))
            }
        }
    }

    private fun updateActionReward() {
        val transformedRewardItem = _uiState.value.currentEditingReward?.toRewardSetupItem()

        viewModelScope.launch {
            if (transformedRewardItem != null) {
                _uiState.update {
                    it.copy(
                        addedRewardItems = it.addedRewardItems.map { listItem -> if (listItem.id == transformedRewardItem.id) transformedRewardItem else listItem },
                        currentEditingReward = null,
                        overlay = null
                    )
                }
                _uiEffect.send(SetupRewardsUiEffect.RewardUpdatedSnackbar(true))
            } else {
                _uiState.update {
                    it.copy(
                        overlay = null
                    )
                }
                _uiEffect.send(SetupRewardsUiEffect.RewardUpdatedSnackbar(false))
            }
        }
    }
}

