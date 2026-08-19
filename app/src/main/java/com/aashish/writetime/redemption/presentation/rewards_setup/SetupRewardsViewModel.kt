package com.aashish.writetime.redemption.presentation.rewards_setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aashish.writetime.redemption.domain.usecase.AddRewardsUseCase
import com.aashish.writetime.redemption.presentation.rewards_setup.RewardsSetupDialogType.*
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
                        dialog = AbortSetupConfirmation
                    )
                }
            }

            SetupRewardsEvent.AddActionRewardClick -> {
                _uiState.update {
                    it.copy(
                        dialog = AddActionReward, currentEditingReward = EditModeRewardItem(
                            Uuid.random().toString(), "", ""
                        )
                    )
                }
            }

            is SetupRewardsEvent.DeleteActionRewardClick -> {
                _uiState.update {
                    it.copy(
                        dialog = DeleteRewardConfirmation(event.reward)
                    )
                }
            }

            SetupRewardsEvent.SaveChangesClick -> {
                _uiState.update {
                    it.copy(
                        dialog = FinishSetupConfirmation
                    )
                }
            }

            is SetupRewardsEvent.RewardItemClick -> {
                _uiState.update {
                    it.copy(
                        dialog = UpdateActionReward,
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

            is SetupRewardsEvent.DialogDismissed -> {
                _uiState.update {
                    it.copy(
                        dialog = null,
                        currentEditingReward = it.currentEditingReward.takeIf { event.dialogType != AddActionReward && event.dialogType != UpdateActionReward },
                    )
                }
            }

            is SetupRewardsEvent.DialogConfirmClicked -> {
                when (event.dialogType) {
                    AbortSetupConfirmation -> {
                        _uiState.update { it.copy(dialog = null) }
                        viewModelScope.launch { _uiEffect.send(SetupRewardsUiEffect.Finish) }
                    }

                    AddActionReward -> {
                        addActionReward()
                    }

                    is DeleteRewardConfirmation -> {
                        _uiState.update { it.copy(
                            dialog = null,
                            addedRewardItems = it.addedRewardItems.mapNotNull {listItem -> if (listItem.id == event.dialogType.reward.id) null else listItem }
                        ) }
                        viewModelScope.launch { _uiEffect.send(SetupRewardsUiEffect.RewardDeletedSnackbar) }
                    }

                    FinishSetupConfirmation -> {
                        viewModelScope.launch {
                            addRewardsUseCase(_uiState.value.addedRewardItems.map { it.toDomainModel() })
                            _uiState.update {
                                it.copy(
                                    dialog = null,
                                )
                            }
                            _uiEffect.send(SetupRewardsUiEffect.Finish)
                        }
                    }

                    UpdateActionReward -> {
                        updateActionReward()
                    }
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
                        dialog = null
                    )
                }
                _uiEffect.send(SetupRewardsUiEffect.RewardAddedSnackbar(true))
            } else {
                _uiState.update {
                    it.copy(
                        dialog = null
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
                        dialog = null
                    )
                }
                _uiEffect.send(SetupRewardsUiEffect.RewardUpdatedSnackbar(true))
            } else {
                _uiState.update {
                    it.copy(
                        dialog = null
                    )
                }
                _uiEffect.send(SetupRewardsUiEffect.RewardUpdatedSnackbar(false))
            }
        }
    }
}

