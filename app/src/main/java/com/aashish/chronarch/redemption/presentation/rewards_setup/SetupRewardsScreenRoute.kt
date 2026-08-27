package com.aashish.chronarch.redemption.presentation.rewards_setup

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.components.ConfirmationDialog
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.redemption.presentation.rewards_setup.components.RewardInputBottomSheet
import com.aashish.chronarch.redemption.presentation.rewards_setup.components.SetupRewardActionsAppBar
import com.aashish.chronarch.redemption.presentation.rewards_setup.components.SetupRewardItem

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun SetupRewardsScreenRoute(
    onFinish: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: SetupRewardsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SetupRewardsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier
    )

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { uiEffect ->
            when (uiEffect) {
                SetupRewardsUiEffect.Finish -> {
                    onFinish()
                }

                SetupRewardsUiEffect.RewardDeletedSnackbar -> snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.deleted_successfully
                    )
                )

                SetupRewardsUiEffect.RewardCostValueErrorSnackbar -> snackbarHostState.showSnackbar(
                    context.getString(R.string.reward_cost_value_error)
                )

                SetupRewardsUiEffect.InvalidRewardInputError -> snackbarHostState.showSnackbar(
                    context.getString(R.string.invalid_reward_input)
                )
            }
        }
    }

    BackHandler {
        viewModel.onEvent(SetupRewardsEvent.BackPressed)
    }
}

@Composable
fun SetupRewardsScreen(
    uiState: SetupRewardsUiState,
    onEvent: (SetupRewardsEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            SetupRewardActionsAppBar(
                isSaveEnabled = uiState.addedRewardItems.isNotEmpty(),
                onCancel = { onEvent(SetupRewardsEvent.AbortSetupClick) },
                onSave = { onEvent(SetupRewardsEvent.SaveChangesClick) },
                title = stringResource(R.string.title_rewards_setup)
            )
        },
        contentWindowInsets = WindowInsets(),
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(space = spacing.medium)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(space = spacing.medium)
            ) {
                items(items = uiState.addedRewardItems, key = { it.id }) { rewardItem ->
                    SetupRewardItem(
                        name = rewardItem.name,
                        value = rewardItem.cost,
                        onItemClick = { onEvent(SetupRewardsEvent.RewardItemClick(rewardItem)) },
                        onDeleteClick = {
                            onEvent(
                                SetupRewardsEvent.DeleteActionRewardClick(
                                    rewardItem
                                )
                            )
                        }
                    )
                }
            }
            uiState.overlay?.let { dialog ->
                when (dialog) {
                    RewardsSetupOverlayType.AbortSetupDialog -> ConfirmationDialog(
                        title = stringResource(
                            R.string.leave_setup_title
                        ),
                        message = stringResource(R.string.leave_setup_message),
                        confirmText = stringResource(R.string.leave_setup),
                        dismissText = stringResource(R.string.continue_setup),
                        onConfirmClick = {
                            onEvent(
                                SetupRewardsEvent.OverlayConfirmClicked(dialog)
                            )
                        },
                        onDismissRequest = {
                            onEvent(
                                SetupRewardsEvent.OverlayDismissed(dialog)
                            )
                        })

                    RewardsSetupOverlayType.AddRewardBottomSheet, RewardsSetupOverlayType.UpdateRewardBottomSheet -> {
                        uiState.currentEditingReward?.let { currentReward ->
                            RewardInputBottomSheet(
                                rewardName = currentReward.name,
                                rewardCost = currentReward.cost,
                                onRewardNameTextChange = {
                                    onEvent(SetupRewardsEvent.CurrentRewardNameChanged(it))
                                },
                                onRewardCostTextChange = {
                                    onEvent(SetupRewardsEvent.CurrentRewardCostChanged(it))
                                },
                                buttonText = stringResource(
                                    when(dialog) {
                                        RewardsSetupOverlayType.AddRewardBottomSheet -> R.string.add
                                        RewardsSetupOverlayType.UpdateRewardBottomSheet -> R.string.update
                                    }
                                ),
                                onDone = {
                                    onEvent(SetupRewardsEvent.OverlayConfirmClicked(dialog))
                                },
                                onCancel = {
                                    onEvent(SetupRewardsEvent.OverlayDismissed(dialog))
                                }
                            )
                        }
                    }

                    is RewardsSetupOverlayType.DeleteRewardDialog -> {
                        ConfirmationDialog(
                            title = stringResource(R.string.delete_reward_title),
                            confirmText = stringResource(R.string.confirm),
                            dismissText = stringResource(R.string.cancel),
                            onConfirmClick = {
                                onEvent(
                                    SetupRewardsEvent.OverlayConfirmClicked(
                                        overlayType = dialog
                                    )
                                )
                            },
                            onDismissRequest = { onEvent(SetupRewardsEvent.OverlayDismissed(dialog)) }
                        )
                    }

                    RewardsSetupOverlayType.FinishSetupDialog -> {
                        ConfirmationDialog(
                            title = stringResource(R.string.save_changes_title),
                            message = stringResource(R.string.save_changes_message),
                            icon = Icons.Default.Lock,
                            confirmText = stringResource(R.string.save),
                            dismissText = stringResource(R.string.cancel),
                            onConfirmClick = {
                                onEvent(
                                    SetupRewardsEvent.OverlayConfirmClicked(
                                        dialog
                                    )
                                )
                            },
                            onDismissRequest = { onEvent(SetupRewardsEvent.OverlayDismissed(dialog)) }
                        )
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(SetupRewardsEvent.AddActionRewardClick) }
            ) {
                Text(
                    text = stringResource(R.string.add_reward),
                    modifier = Modifier.padding(spacing.small),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun SetupRewardsScreenPreview() {
    ChronarchTheme {
        SetupRewardsScreen(SetupRewardsUiState(), {})
    }
}