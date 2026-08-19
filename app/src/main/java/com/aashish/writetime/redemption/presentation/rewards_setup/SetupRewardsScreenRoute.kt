package com.aashish.writetime.redemption.presentation.rewards_setup

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
import androidx.compose.material.icons.filled.Delete
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
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.ConfirmationDialog
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.redemption.presentation.rewards_setup.components.RewardInputBottomSheet
import com.aashish.writetime.redemption.presentation.rewards_setup.components.SetupRewardActionsAppBar
import com.aashish.writetime.redemption.presentation.rewards_setup.components.SetupRewardItem

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
                is SetupRewardsUiEffect.RewardAddedSnackbar -> snackbarHostState.showSnackbar(
                    context.getString(
                        if (uiEffect.isSuccess) R.string.added_successfully else R.string.addition_failed
                    )
                )

                SetupRewardsUiEffect.RewardDeletedSnackbar -> snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.deleted_successfully
                    )
                )

                is SetupRewardsUiEffect.RewardUpdatedSnackbar -> snackbarHostState.showSnackbar(
                    context.getString(
                        if (uiEffect.isSuccess) R.string.updated_successfully else R.string.updated_failed
                    )
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
            uiState.dialog?.let { dialog ->
                when (dialog) {
                    RewardsSetupDialogType.AbortSetupConfirmation -> ConfirmationDialog(
                        title = stringResource(
                            R.string.leave_setup_title
                        ),
                        message = stringResource(R.string.leave_setup_message),
                        positiveCtaText = stringResource(R.string.leave_setup),
                        negativeCtaText = stringResource(R.string.continue_setup),
                        onPositiveClick = {
                            onEvent(
                                SetupRewardsEvent.DialogConfirmClicked(dialog)
                            )
                        },
                        onNegativeClick = {
                            onEvent(
                                SetupRewardsEvent.DialogDismissed(dialog)
                            )
                        })

                    RewardsSetupDialogType.AddActionReward -> {
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
                                buttonText = stringResource(R.string.add),
                                onDone = {
                                    onEvent(SetupRewardsEvent.DialogConfirmClicked(dialog))
                                },
                                onCancel = {
                                    onEvent(SetupRewardsEvent.DialogDismissed(dialog))
                                }
                            )
                        }
                    }

                    is RewardsSetupDialogType.DeleteRewardConfirmation -> {
                        ConfirmationDialog(
                            title = stringResource(R.string.delete_reward_title),
                            icon = Icons.Default.Delete,
                            positiveCtaText = stringResource(R.string.action_yes),
                            negativeCtaText = stringResource(R.string.action_no),
                            onPositiveClick = {
                                onEvent(
                                    SetupRewardsEvent.DialogConfirmClicked(
                                        dialogType = dialog
                                    )
                                )
                            },
                            onNegativeClick = { onEvent(SetupRewardsEvent.DialogDismissed(dialog)) }
                        )
                    }

                    RewardsSetupDialogType.FinishSetupConfirmation -> {
                        ConfirmationDialog(
                            title = stringResource(R.string.save_changes_title),
                            message = stringResource(R.string.save_changes_message),
                            icon = Icons.Default.Lock,
                            positiveCtaText = stringResource(R.string.save),
                            negativeCtaText = stringResource(R.string.cancel),
                            onPositiveClick = {
                                onEvent(
                                    SetupRewardsEvent.DialogConfirmClicked(
                                        dialog
                                    )
                                )
                            },
                            onNegativeClick = { onEvent(SetupRewardsEvent.DialogDismissed(dialog)) }
                        )
                    }

                    RewardsSetupDialogType.UpdateActionReward -> {
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
                                buttonText = stringResource(R.string.update),
                                onDone = {
                                    onEvent(SetupRewardsEvent.DialogConfirmClicked(dialog))
                                },
                                onCancel = {
                                    onEvent(SetupRewardsEvent.DialogDismissed(dialog))
                                }
                            )
                        }
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
    WriteTimeTheme {
        SetupRewardsScreen(SetupRewardsUiState(), {})
    }
}