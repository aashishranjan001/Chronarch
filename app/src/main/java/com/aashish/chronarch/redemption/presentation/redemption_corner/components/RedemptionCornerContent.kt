package com.aashish.chronarch.redemption.presentation.redemption_corner.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.components.ConfirmationBottomSheet
import com.aashish.chronarch.common.ui.components.NoDataScreen
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.redemption.presentation.redemption_corner.RedemptionCornerEvent
import com.aashish.chronarch.redemption.presentation.redemption_corner.RedemptionCornerUiState

@Composable
fun RedemptionCornerContent(
    uiState: RedemptionCornerUiState,
    onEvent: (RedemptionCornerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Scaffold(
        modifier = modifier,
        topBar = {
            RedemptionCornerTopAppBar(
                title = stringResource(R.string.title_redemption_corner),
                balance = uiState.availableFocusPointsBalance
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.showRewardsSetup) {
                NoDataScreen(
                    thumbnailResId = R.drawable.pending_setup,
                    contentDescription = stringResource(R.string.setup_actions_message) ,
                    title = stringResource(R.string.setup_actions_message),
                    message = stringResource(R.string.note_one_time_action),
                    actionText = stringResource(R.string.proceed),
                    actionClick = { onEvent(RedemptionCornerEvent.SetupRewardActions) },
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(spacing.medium),
                    horizontalAlignment = Alignment.CenterHorizontally,
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
private fun RedemptionCornerContentPreview() {
    ChronarchTheme {
        RedemptionCornerContent(
            uiState = RedemptionCornerUiState(),
            onEvent = {}
        )
    }
}