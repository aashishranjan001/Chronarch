package com.aashish.writetime.history.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.components.HistoryScreenTopBar
import com.aashish.writetime.history.presentation.components.SessionsSection
import com.aashish.writetime.history.presentation.components.TransactionsSection
import com.aashish.writetime.history.presentation.model.HistoryTab
import com.aashish.writetime.history.presentation.model.HistoryUiState

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

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { uiEffect ->
            when (uiEffect) {
                is HistoryUiEffect.ScrollToTab -> {
                    pagerState.animateScrollToPage(uiEffect.tabIndex)
                }
            }
        }
    }

    HistoryScreen(pagerState, uiState, viewModel::onEvent, modifier)
}

@Composable
fun HistoryScreen(
    pagerState: PagerState,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HistoryScreenTopBar(
            tabList = uiState.tabs,
            currentPage = pagerState.currentPage,
            onTabClick = {
                onEvent(HistoryEvent.TabSelect(it))
            }
        )

        HorizontalPager(
            state = pagerState
        ) { page ->
            when (uiState.tabs[page]) {
                HistoryTab.SESSIONS -> SessionsSection(uiState.sessions)
                HistoryTab.TRANSACTIONS -> TransactionsSection(uiState.transactions)
            }
        }
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { HistoryTab.entries.size }
    )
    WriteTimeTheme {
        HistoryScreen(pagerState, HistoryUiState(HistoryTab.entries), {})
    }
}