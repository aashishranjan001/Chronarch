package com.aashish.writetime.common.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmationDialog(
    title: String,
    message: String? = null,
    icon: ImageVector? = null,
    confirmText: String,
    dismissText: String? = null,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: (() -> Unit)? = null
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        icon = icon?.let {
            {
                Icon(imageVector = it, contentDescription = "Dialog Icon")
            }
        },
        text = message?.let {
            {
                Text(text = it)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            dismissText?.let {
                TextButton(
                    onClick = onDismissClick ?: onDismissRequest
                ) {
                    Text(text = it)
                }
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@Preview
@Composable
private fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        title = "Are you sure you want to live?",
        message = "I don't think there is any need for that",
        confirmText = "Yes",
        dismissText = "No",
        onConfirmClick = {},
        onDismissClick = {},
        onDismissRequest = {}
    )
}