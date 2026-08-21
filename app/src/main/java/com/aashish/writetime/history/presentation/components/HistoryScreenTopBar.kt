package com.aashish.writetime.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.model.HistoryTab

@Composable
fun HistoryScreenTopBar(
    tabList: List<HistoryTab>,
    currentPage: Int,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        PrimaryTabRow(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
            divider = {},
            selectedTabIndex = currentPage,
            modifier = modifier
                .fillMaxWidth(0.6f)
                .clip(CircleShape)
                .align(
                    Alignment.Center
                ),
            indicator = {}
        ) {
            tabList.forEachIndexed { index, tab ->
                val isSelected = currentPage == index
                Tab(
                    modifier = Modifier
                        .padding(spacing.extraSmall)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.outline else Color.Transparent,
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    selected = isSelected,
                    onClick = {
                        onTabClick(index)
                    },
                    text = {
                        when (tab) {
                            HistoryTab.TRANSACTIONS -> Text(
                                text = stringResource(R.string.transactions),
                            )

                            HistoryTab.SESSIONS -> Text(
                                text = stringResource(R.string.sessions),
                            )
                        }
                    }
                )
            }
        }

        // Filter icon and popup
        Box(modifier = Modifier.align(Alignment.CenterEnd).padding(horizontal = spacing.medium)) {
            IconButton(
                onClick = { } //
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = stringResource(R.string.filter_icon)
                )
            }
        }
    }
}


@Preview
@Composable
private fun HistoryScreenTopBarPreview() {
    WriteTimeTheme {
        HistoryScreenTopBar(
            HistoryTab.entries, 1, {}
        )
    }
}