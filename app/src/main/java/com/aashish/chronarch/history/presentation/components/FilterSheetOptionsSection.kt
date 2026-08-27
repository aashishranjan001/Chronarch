package com.aashish.chronarch.history.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.common.ui.toReadableLocalDate
import com.aashish.chronarch.history.presentation.model.FilterOption
import com.aashish.chronarch.history.presentation.model.FilterOptionItem

@Composable
fun FilterSheetOptionsSection(
    options: List<FilterOptionItem>,
    onFilterCheckboxOptionClicked: (FilterOptionItem.Checkbox, Boolean) -> Unit,
    onFilterRadioOptionClicked: (FilterOptionItem.Radio) -> Unit,
    onCustomDateRangeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current

    LazyColumn(
        contentPadding = PaddingValues(spacing.medium),
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        options.forEach { option ->
            item {
                when (option) {
                    is FilterOptionItem.DateRange -> {
                        DateRangeFilterOption(
                            option = option,
                            onClick = onCustomDateRangeClicked,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    is FilterOptionItem.Radio -> {
                        val labelRes = when (option.option) {
                            FilterOption.Date.ThisMonth -> R.string.option_date_this_month
                            FilterOption.Date.ThisWeek -> R.string.option_date_this_week
                            else -> null
                        }
                        labelRes?.let {
                            RadioFilterOption(
                                labelRes = it,
                                selected = option.isSelected,
                                onClick = { onFilterRadioOptionClicked(option) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    is FilterOptionItem.Checkbox -> {
                        val labelRes = when (option.option) {
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
                                onCheckedChange = { onFilterCheckboxOptionClicked(option, it) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                    }
                }

            }
        }
    }
}

@Composable
fun DateRangeFilterOption(
    option: FilterOptionItem.DateRange,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = stringResource(R.string.choose_date)
            )
        }
        TextButton(onClick = onClick) {
            if (option.startDate != null && option.endDate != null) {
                Text(
                    text = stringResource(
                        R.string.date_from_x_to_y,
                        option.startDate.toReadableLocalDate(),
                        option.endDate.toReadableLocalDate()
                    ),
                    style = MaterialTheme.typography.titleSmall
                )
            } else {
                Text(
                    stringResource(R.string.option_date_custom_range),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun RadioFilterOption(
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            modifier = Modifier,
            selected = selected,
            onClick = onClick
        )
        TextButton(
            onClick = onClick
        ) {
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


@Composable
fun CheckboxFilterOption(
    @StringRes labelRes: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            modifier = Modifier,
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) }
        )
        TextButton(
            onClick = { onCheckedChange(!isChecked) }
        ) {
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview
@Composable
private fun FilterSheetOptionsSectionPreview() {
    ChronarchTheme {
        FilterSheetOptionsSection(
            options = listOf(
                FilterOptionItem.Checkbox(
                    FilterOption.SessionFilter.CompletionStatus.Finished,
                    true
                ),
                FilterOptionItem.Checkbox(FilterOption.SessionFilter.DurationType.LongType, false),
                FilterOptionItem.DateRange(null, null)
            ),
            onFilterCheckboxOptionClicked = { _, _ -> },
            onFilterRadioOptionClicked = {},
            onCustomDateRangeClicked = {}
        )
    }
}