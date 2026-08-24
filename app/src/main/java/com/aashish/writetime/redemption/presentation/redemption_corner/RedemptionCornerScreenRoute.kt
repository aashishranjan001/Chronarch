package com.aashish.writetime.redemption.presentation.redemption_corner

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.ConfirmationBottomSheet
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.redemption.presentation.redemption_corner.components.RedemptionCornerTopAppBar
import com.aashish.writetime.redemption.presentation.redemption_corner.components.RewardItem
import com.aashish.writetime.redemption.presentation.redemption_corner.components.RewardsSetupPendingContent


@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun RedemptionCornerScreenRoute(
    snackbarHostState: SnackbarHostState,
    onLaunchRewardsSetup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RedemptionCornerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RedemptionCornerScreen(uiState, viewModel::onEvent, modifier)

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { uiEffect ->
            when (uiEffect) {
                RedemptionCornerUiEffect.LaunchRewardsSetup -> {
                    onLaunchRewardsSetup()
                }

                is RedemptionCornerUiEffect.ShowRewardsRedeemedMessage -> {
                    snackbarHostState.showSnackbar(
                        context.getString(
                            R.string.reward_redeemed_congratulations_message,
                            uiEffect.rewardName
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RedemptionCornerScreen(
    uiState: RedemptionCornerUiState,
    onEvent: (RedemptionCornerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            RedemptionCornerTopAppBar(
                title = stringResource(R.string.title_redemption_corner),
                balance = uiState.availableFocusPointsBalance
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.showRewardsSetup) {
                RewardsSetupPendingContent(
                    onProceedClick = { onEvent(RedemptionCornerEvent.SetupRewardActions) },
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                )
            } else {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(spacing.medium)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(space = spacing.medium)
                ) {
                    items(items = uiState.rewardsList, key = { it.id }) { reward ->
                        RewardItem(
                            name = reward.name,
                            value = reward.cost,
                            isEnabled = reward.isRedeemable,
                            onItemClick = {
                                onEvent(
                                    RedemptionCornerEvent.RewardActionClick(reward)
                                )
                            })
                    }
                }
                uiState.selectedRedeemableReward?.let {
                    ConfirmationBottomSheet(
                        heading = stringResource(R.string.title_redeem_focus_points),
                        message = stringResource(
                            R.string.message_redeem_x_points_for_y_action,
                            it.cost,
                            it.name
                        ),
                        confirmText = stringResource(R.string.confirm),
                        dismissText = stringResource(R.string.cancel),
                        onDismissRequest = {
                            onEvent(RedemptionCornerEvent.DismissRewardsRedeemDialog)
                        },
                        onConfirmClick = {
                            onEvent(
                                RedemptionCornerEvent.RewardsRedeemDialogOptionClick(true)
                            )
                        },
                        onDismissClick = {
                            onEvent(
                                RedemptionCornerEvent.RewardsRedeemDialogOptionClick(false)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RedemptionCornerScreenPreview() {
    WriteTimeTheme {
        RedemptionCornerScreen(
            RedemptionCornerUiState(),
            {}
        )
    }
}