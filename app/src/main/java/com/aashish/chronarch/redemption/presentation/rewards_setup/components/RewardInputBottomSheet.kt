package com.aashish.chronarch.redemption.presentation.rewards_setup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardInputBottomSheet(
    rewardName: String,
    rewardCost: String,
    onRewardNameTextChange: (String) -> Unit,
    onRewardCostTextChange: (String) -> Unit,
    buttonText: String,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        sheetState = bottomSheetState,
        onDismissRequest = onCancel,
    ) {

        val nameFocusRequester = remember { FocusRequester() }
        val costFocusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            nameFocusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(spacing.medium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(
                space = spacing.medium,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameFocusRequester),
                maxLines = 1,
                value = rewardName, onValueChange = onRewardNameTextChange, label = {
                    Text(text = stringResource(R.string.reward_name_text_label))
                },
                placeholder = {
                    Text(text = stringResource(R.string.reward_name_text_input_placeholder))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { costFocusRequester.requestFocus() }
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = spacing.medium,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(costFocusRequester)
                        .weight(3f)
                        .fillMaxHeight(),
                    maxLines = 1,
                    value = rewardCost, onValueChange = onRewardCostTextChange, label = {
                        Text(text = stringResource(R.string.reward_cost_text_label))
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.reward_cost_text_input_placeholder))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Button(
                    modifier = Modifier
                        .weight(2f),
                    onClick = onDone,
                    enabled = rewardName.isNotBlank() && rewardCost.isNotBlank()
                ) {
                    Text(
                        text = buttonText,
                        modifier = Modifier.padding(spacing.small),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RewardInputBottomSheetPreview() {
    RewardInputBottomSheet(
        rewardName = "32",
        rewardCost = "23",
        onRewardNameTextChange = { },
        onRewardCostTextChange = { },
        buttonText = "Save",
        onDone = {},
        onCancel = { },
    )
}