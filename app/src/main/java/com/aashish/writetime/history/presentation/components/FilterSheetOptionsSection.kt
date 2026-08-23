package com.aashish.writetime.history.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.common.ui.toHumanReadableDate
import com.aashish.writetime.history.presentation.model.FilterOption
import com.aashish.writetime.history.presentation.model.FilterOptionType

@Composable
fun FilterSheetOptionsSection(
    options: List<FilterOptionType>,
    onFilterOptionClicked: (FilterOption, Boolean) -> Unit,
    onCustomDateRangeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current

    LazyColumn(modifier = modifier
        .fillMaxWidth()
        .padding(spacing.medium), verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        options.forEach { option ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    when(option) {
                        is FilterOptionType.DateRange -> {
                            Row(modifier = Modifier.clickable {
                                onCustomDateRangeClicked()
                            }, verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = onCustomDateRangeClicked
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = stringResource(R.string.choose_date)
                                    )
                                }

                                TextButton(
                                    onClick = onCustomDateRangeClicked
                                ) {
                                    if (option.startDate != null && option.endDate != null) {
                                        Text(
                                            stringResource(
                                                R.string.date_x_from_y,
                                                option.startDate.toHumanReadableDate(),
                                                option.endDate.toHumanReadableDate()
                                            )
                                        )
                                    } else {
                                        Text(stringResource(R.string.option_date_custom_range))
                                    }
                                }
                            }

                        }
                        is FilterOptionType.Radio -> {
                            val labelRes = when (option.option) {
                                FilterOption.Date.ThisMonth -> R.string.option_date_this_month
                                FilterOption.Date.ThisWeek -> R.string.option_date_this_week
                                else -> null
                            }
                            labelRes?.let {
                                RadioFilterOption(
                                    labelRes = it,
                                    selected = option.isSelected,
                                    onClick = { onFilterOptionClicked(option.option, !option.isSelected) }
                                )
                            }
                        }
                        is FilterOptionType.Checkbox -> {
                            val labelRes = when(option.option) {
                                FilterOption.SessionFilter.CompletionStatus.Cancelled -> R.string.option_failed_completion_status
                                FilterOption.SessionFilter.CompletionStatus.Finished -> R.string.option_successful_completion_status
                                FilterOption.SessionFilter.DurationType.LongType -> R.string.option_long_duration
                                FilterOption.SessionFilter.DurationType.ShortType -> R.string.option_short_duration
                                FilterOption.TransactionFilter.Type.TaskCredit -> R.string.option_task_credit_earning
                                FilterOption.TransactionFilter.Type.Redemption -> R.string.option_redemption_debit
                                FilterOption.TransactionFilter.Type.Bonus -> R.string.option_bonus_earning
                                else -> null
                            }

                            labelRes?.let {
                                CheckboxFilterOption(
                                    labelRes = it,
                                    option.isSelected,
                                    onCheckedChange = { onFilterOptionClicked(option.option, it) })
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
                FilterOptionType.Checkbox(FilterOption.SessionFilter.CompletionStatus.Finished, true),
                FilterOptionType.Checkbox(FilterOption.SessionFilter.DurationType.LongType, false),
                FilterOptionType.DateRange(null, null)
            ),
            onFilterOptionClicked = {_, _ -> },
            onCustomDateRangeClicked = {}
        )
    }
}