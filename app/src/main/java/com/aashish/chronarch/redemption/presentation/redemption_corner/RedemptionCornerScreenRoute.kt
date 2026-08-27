package com.aashish.chronarch.redemption.presentation.redemption_corner

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.common.ui.components.NoDataScreen
import com.aashish.chronarch.redemption.presentation.redemption_corner.components.RedemptionCornerContent


@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun RedemptionCornerScreenRoute(
    snackbarHostState: SnackbarHostState,
    onLaunchRewardsSetup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RedemptionCornerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

    RedemptionCornerScreen(uiState = uiState, onEvent = viewModel::onEvent, modifier = modifier)
}

@Composable
fun RedemptionCornerScreen(
    uiState: RedemptionCornerUiState,
    onEvent: (RedemptionCornerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.isError) (
            NoDataScreen(
                thumbnailResId = R.drawable.app_error,
                contentDescription = stringResource(R.string.internal_error),
                title = stringResource(R.string.internal_error),
                message = stringResource(R.string.rewards_fetch_error_message),
                actionText = stringResource(R.string.retry),
                actionClick = {
                    onEvent(RedemptionCornerEvent.RetryClick)
                },
                modifier = modifier.fillMaxSize()
            )
    ) else {
        RedemptionCornerContent(
            uiState = uiState,
            onEvent = onEvent,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun RedemptionCornerScreenPreview() {
    ChronarchTheme {
        RedemptionCornerScreen(
            uiState = RedemptionCornerUiState(),
            onEvent = {}
        )
    }
}