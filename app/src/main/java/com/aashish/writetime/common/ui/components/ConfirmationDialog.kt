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
    positiveCtaText: String,
    negativeCtaText: String,
    onDismissRequest: (() -> Unit)? = null,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
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
                onClick = onPositiveClick
            ) {
                Text(text = positiveCtaText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onNegativeClick
            ) {
                Text(text = negativeCtaText)
            }
        },
        onDismissRequest = onDismissRequest ?: onNegativeClick
    )
}

@Preview
@Composable
private fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        title = "Are you sure you want to live?",
        message = "I don't think there is any need for that",
        positiveCtaText = "Yes",
        negativeCtaText = "No",
        onPositiveClick = {},
        onNegativeClick = {},
        onDismissRequest = {}
    )
}