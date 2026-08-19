package com.aashish.writetime.redemption.presentation.rewards_setup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing

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
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onCancel,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(
                space = spacing.medium,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f),
                maxLines = 1,
                value = rewardName, onValueChange = onRewardNameTextChange, label = {
                    Text(text = stringResource(R.string.reward_name_text_label))
                },
                placeholder = {
                    Text(text = stringResource(R.string.reward_name_text_input_placeholder))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = spacing.medium,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(3f).fillMaxHeight(),
                    maxLines = 1,
                    value = rewardCost, onValueChange = onRewardCostTextChange, label = {
                        Text(text = stringResource(R.string.reward_cost_text_label))
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.reward_cost_text_input_placeholder))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )
                Button(
                    modifier = Modifier.weight(2f).fillMaxHeight(),
                    onClick = onDone,
                    enabled = rewardName.isNotBlank() && rewardCost.isNotBlank()
                ) {
                    Text(text = buttonText)
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