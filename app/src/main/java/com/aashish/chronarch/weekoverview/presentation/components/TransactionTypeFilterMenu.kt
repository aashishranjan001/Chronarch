package com.aashish.chronarch.weekoverview.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.weekoverview.presentation.FocusPointsViewFilter

@Composable
fun TransactionTypeFilterMenu(
    selectedOption: FocusPointsViewFilter,
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    onMenuOptionSelected: (FocusPointsViewFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    DropdownMenu(
        modifier = modifier,
        expanded = isExpanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.transaction_filter_menu_item_all))
            },
            onClick = {
                onMenuOptionSelected(FocusPointsViewFilter.ALL)
            },
            leadingIcon = {
                if (selectedOption == FocusPointsViewFilter.ALL) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.transaction_filter_menu_item_all)
                    )
                }
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.transaction_filter_menu_item_task_credit))
            },
            onClick = {
                onMenuOptionSelected(FocusPointsViewFilter.TASK_CREDIT)
            },
            leadingIcon = {
                if (selectedOption == FocusPointsViewFilter.TASK_CREDIT) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.transaction_filter_menu_item_task_credit)
                    )
                }
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.transaction_filter_menu_item_bonus))
            },
            onClick = {
                onMenuOptionSelected(FocusPointsViewFilter.BONUS)
            },
            leadingIcon = {
                if (selectedOption == FocusPointsViewFilter.BONUS) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.transaction_filter_menu_item_bonus)
                    )
                }
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.transaction_filter_menu_item_redeem))
            },
            onClick = {
                onMenuOptionSelected(FocusPointsViewFilter.REDEEM)
            },
            leadingIcon = {
                if (selectedOption == FocusPointsViewFilter.REDEEM) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.transaction_filter_menu_item_redeem)
                    )
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun TransactionTypeFilterMenuPreview() {
    ChronarchTheme {
        TransactionTypeFilterMenu(
            FocusPointsViewFilter.ALL,
            true, {}, {}
        )
    }
}