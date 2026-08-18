package com.aashish.writetime.weekoverview.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.weekoverview.presentation.FocusPointsViewFilter

@Composable
fun FocusPointsTransactionsGraphCard(
    dailyTransactionPointsMap: Map<String, Int>,
    showFilterMenuExpanded: Boolean,
    onFilterMenuDismissed: () -> Unit,
    onFilterMenuIconClick: () -> Unit,
    selectedMenuOption: FocusPointsViewFilter,
    onFilterMenuOptionSelected: (FocusPointsViewFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Card(modifier = modifier, shape = RoundedCornerShape(spacing.medium)) {
        Column(
            modifier = Modifier
                .padding(spacing.medium)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.focus_points_activity),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(spacing.medium))
                Box {
                    FilledTonalIconButton(
                        onClick = onFilterMenuIconClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = stringResource(R.string.filter_icon)
                        )
                    }
                    TransactionTypeFilterMenu(
                        selectedOption = selectedMenuOption,
                        isExpanded = showFilterMenuExpanded,
                        onDismissRequest = onFilterMenuDismissed,
                        onMenuOptionSelected = onFilterMenuOptionSelected
                    )
                }
            }
            BarGraph(
                valuesMap = dailyTransactionPointsMap,
                modifier = Modifier.height(
                    if (dailyTransactionPointsMap.values.all { it == 0 }) 150.dp else 300.dp
                )
            )
        }
    }
}

@Preview
@Composable
private fun FocusPointsTransactionsGraphCardPreview() {
    WriteTimeTheme {
        FocusPointsTransactionsGraphCard(emptyMap(), true, {}, {}, FocusPointsViewFilter.ALL, {})
    }
}