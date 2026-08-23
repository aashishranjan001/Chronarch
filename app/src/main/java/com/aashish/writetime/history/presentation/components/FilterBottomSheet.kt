package com.aashish.writetime.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.model.FilterCategory
import com.aashish.writetime.history.presentation.model.FilterOption
import com.aashish.writetime.history.presentation.model.FilterOptionType
import kotlin.to

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    categories: Map<FilterCategory, Boolean>,
    options: List<FilterOptionType>,
    selectedCategory: FilterCategory?,
    onDismiss: () -> Unit,
    onClearFilter: () -> Unit,
    onApplyFilter: () -> Unit,
    onFilterCategoryClick: (FilterCategory) -> Unit,
    onFilterOptionClick: (FilterOption, Boolean) -> Unit,
    onCustomDateRangeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        sheetState = bottomSheetState,
        onDismissRequest = onDismiss,
        dragHandle = {}
    ) {
        LazyColumn (modifier = modifier) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(spacing.medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.filter),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    FilterSheetCategoriesSection(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategoryClick = onFilterCategoryClick,
                        modifier = Modifier.weight(4f).fillMaxHeight()
                    )
                    VerticalDivider(modifier = Modifier.width(2.dp))
                    FilterSheetOptionsSection(
                        options = options,
                        onFilterOptionClicked = onFilterOptionClick,
                        onCustomDateRangeClicked = onCustomDateRangeClicked,
                        modifier = Modifier.weight(6f).fillMaxHeight()

                    )
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.medium),
                    horizontalArrangement = Arrangement.spacedBy(spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onClearFilter,
                        enabled = categories.values.any { it }
                    ) {
                        Text(
                            text = stringResource(R.string.clear_filter),
                            modifier = Modifier.padding(spacing.small),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onApplyFilter
                    ) {
                        Text(
                            text = stringResource(R.string.apply_filters),
                            modifier = Modifier.padding(spacing.small),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterBottomSheetPreview() {
    WriteTimeTheme {
        FilterBottomSheet(
            categories = mapOf(
                FilterCategory.SessionFilter.CompletionStatus to true,
                FilterCategory.SessionFilter.DurationType to false
            ),
            options = listOf(
                FilterOptionType.Checkbox(FilterOption.SessionFilter.CompletionStatus.Finished, true),
                FilterOptionType.Checkbox(FilterOption.SessionFilter.DurationType.LongType, false)
            ),
            selectedCategory = FilterCategory.SessionFilter.CompletionStatus,
            onDismiss = {},
            onClearFilter = {},
            onApplyFilter = {},
            onFilterOptionClick = { _, _ ->},
            onFilterCategoryClick = {},
            onCustomDateRangeClicked = {}
        )
    }
}