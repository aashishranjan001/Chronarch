package com.aashish.writetime.common.ui.components

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun IconText(
    icon: ImageVector,
    contentDescription: String,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    modifier: Modifier = Modifier
) {
    val iconContentId = "icon_content"
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            appendInlineContent(iconContentId)
            append("  ")
            append(text)
        },
        style = textStyle,
        inlineContent = mapOf(
            iconContentId to InlineTextContent(
                placeholder = Placeholder(
                    width = textStyle.fontSize,
                    height = textStyle.fontSize,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Icon(imageVector = icon, contentDescription = contentDescription)
            }
        )
    )
}

@Preview
@Composable
private fun IconTextPreview() {
    WriteTimeTheme {
        IconText(
            Icons.Default.DateRange, "Date", text = "25 Aug, 2026"
        )
    }
}