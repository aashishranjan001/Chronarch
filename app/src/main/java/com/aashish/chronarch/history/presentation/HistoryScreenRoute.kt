package com.aashish.chronarch.history.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.components.NoDataScreen
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.history.presentation.components.HistoryContent
import com.aashish.chronarch.history.presentation.model.HistoryTab
import com.aashish.chronarch.history.presentation.model.HistoryUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HistoryScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        pageCount = {
            uiState.tabs.size
        }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.distinctUntilChanged()
            .collectLatest { page ->
                viewModel.onEvent(HistoryEvent.SaveSelectedTab(tabIndex = page))
            }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { uiEffect ->
            when (uiEffect) {
                is HistoryUiEffect.ScrollToTab -> {
                    pagerState.animateScrollToPage(uiEffect.tabIndex)
                }
            }
        }
    }

    HistoryScreen(
        pagerState = pagerState,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun HistoryScreen(
    pagerState: PagerState,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    if (uiState.isLoading) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.isError) {
        NoDataScreen(
            thumbnailResId = R.drawable.app_error,
            contentDescription = stringResource(R.string.internal_error),
            title = stringResource(R.string.internal_error),
            message = stringResource(R.string.history_fetch_error_message),
            actionText = stringResource(R.string.retry),
            actionClick = {
                onEvent(HistoryEvent.RetryClick)
            })
    } else {
        HistoryContent(
            pagerState = pagerState,
            uiState = uiState,
            onEvent = onEvent,
            modifier = modifier.fillMaxSize())
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { HistoryTab.entries.size }
    )
    ChronarchTheme {
        HistoryScreen(pagerState, HistoryUiState(
            isLoading = false,
            isError = false,
            tabs = HistoryTab.entries,
            selectedTab = HistoryTab.SESSIONS
        ), {})
    }
}