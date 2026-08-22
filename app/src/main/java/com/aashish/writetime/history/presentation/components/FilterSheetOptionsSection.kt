package com.aashish.writetime.history.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import com.aashish.writetime.history.presentation.model.FilterOption
import com.aashish.writetime.history.presentation.model.FilterOptionState

@Composable
fun FilterSheetOptionsSection(
    options: List<FilterOptionState>,
    onFilterOptionClicked: (FilterOption, Boolean) -> Unit,
    onDateFilterOptionSelected: (FilterOption) -> Unit,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        options.forEach { option ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(spacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    when(option) {
                        is FilterOptionState.DateFilterOptionState.CustomRange -> {}
                        is FilterOptionState.DateFilterOptionState.FixedRange -> {
                            when(option.option) {
                                FilterOption.SessionFilter.Date.ThisMonth, FilterOption.TransactionFilter.Date.ThisMonth -> {
                                    RadioFilterOption(R.string.option_date_this_month,
                                        selected = option.isSelected,
                                        onClick = { onDateFilterOptionSelected(option.option) }
                                    )
                                }
                                FilterOption.SessionFilter.Date.ThisWeek, FilterOption.TransactionFilter.Date.ThisWeek -> {
                                    RadioFilterOption(R.string.option_date_this_week,
                                        selected = option.isSelected,
                                        onClick = { onDateFilterOptionSelected(option.option) }
                                    )
                                }
                                else -> {}
                            }
                        }
                        is FilterOptionState.SelectionFilterOptionState -> {
                            when(option.option) {
                                FilterOption.SessionFilter.CompletionStatus.Cancelled -> {
                                    CheckboxFilterOption(
                                        R.string.option_failed_completion_status,
                                        option.isSelected,
                                        onCheckedChange = { onFilterOptionClicked(option.option, it) })
                                }
                                FilterOption.SessionFilter.CompletionStatus.Finished -> {
                                    CheckboxFilterOption(
                                        R.string.option_successful_completion_status,
                                        option.isSelected,
                                        onCheckedChange = { onFilterOptionClicked(option.option, it) })
                                }
                                FilterOption.SessionFilter.DurationType.LongType -> {
                                    CheckboxFilterOption(
                                        R.string.option_long_duration,
                                        option.isSelected,
                                        onCheckedChange = { onFilterOptionClicked(option.option, it) })
                                }
                                FilterOption.SessionFilter.DurationType.ShortType -> {
                                    CheckboxFilterOption(
                                        R.string.option_short_duration,
                                        option.isSelected,
                                        onCheckedChange = { onFilterOptionClicked(option.option, it) })
                                }
                                FilterOption.TransactionFilter.Type.TaskCredit -> {
                                    CheckboxFilterOption(
                                        R.string.option_task_credit_earning,
                                        option.isSelected,
                                        onCheckedChange = { onFilterOptionClicked(option.option, it) })
                                }
                                FilterOption.TransactionFilter.Type.Redemption -> {
                                    CheckboxFilterOption(
                                        R.string.option_redemption_debit,
                                        option.isSelected,
                                        onCheckedChange = { onFilterOptionClicked(option.option, it) })
                                }
                                FilterOption.TransactionFilter.Type.Bonus -> {
                                    CheckboxFilterOption(
                                        R.string.option_bonus_earning,
                                        option.isSelected,
                                        onCheckedChange = { onFilterOptionClicked(option.option, it) })
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RadioFilterOption(
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier) {

    RadioButton(
        modifier = modifier,
        selected = selected,
        onClick = onClick
    )
    Spacer(modifier = Modifier.width(LocalSpacing.current.small))
    Text(
        text = stringResource(labelRes),
        style = MaterialTheme.typography.bodyMedium
    )
}


@Composable
fun CheckboxFilterOption(
    @StringRes labelRes: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier) {

    Checkbox(
        modifier = modifier,
        checked = isChecked,
        onCheckedChange = { onCheckedChange(it) }
    )
    Spacer(modifier = Modifier.width(LocalSpacing.current.small))
    Text(
        text = stringResource(labelRes),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
private fun FilterSheetOptionsSectionPreview() {
    WriteTimeTheme {
        FilterSheetOptionsSection(
            options = listOf(
                FilterOptionState.SelectionFilterOptionState(FilterOption.SessionFilter.CompletionStatus.Finished, true),
                FilterOptionState.SelectionFilterOptionState(FilterOption.SessionFilter.DurationType.LongType, false)
            ),
            onFilterOptionClicked = {_, _ -> },
            onDateFilterOptionSelected = {}
        )
    }
}