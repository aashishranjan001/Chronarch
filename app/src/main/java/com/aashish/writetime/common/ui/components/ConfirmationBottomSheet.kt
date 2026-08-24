package com.aashish.writetime.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationBottomSheet(
    heading: String,
    message: String,
    confirmText: String,
    dismissText: String? = null,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(
                space = spacing.medium,
                alignment = Alignment.CenterVertically
            )
        ) {
            Text(
                text = heading,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(space = spacing.medium, alignment = Alignment.CenterHorizontally)) {
                dismissText?.let {
                    OutlinedButton (
                        modifier = Modifier.weight(1f),
                        onClick = onDismissClick ?: onDismissRequest
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(spacing.small),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onConfirmClick
                ) {
                    Text(
                        text = confirmText,
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
private fun ConfirmationBottomSheetPreview() {
    WriteTimeTheme {
        ConfirmationBottomSheet(
            heading = "Redeem Reward?",
            message = "Redeem for 4 points",
            confirmText = "Yes",
            dismissText = "No",
            onConfirmClick = {},
            onDismissClick = {},
            onDismissRequest = {}
        )
    }
}