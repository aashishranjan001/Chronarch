package com.aashish.chronarch.common.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.theme.ChronarchTheme

@Composable
fun NoDataScreen(
    @DrawableRes thumbnailResId: Int,
    contentDescription: String? = null,
    title: String,
    message: String? = null,
    actionText: String? = null,
    actionClick: (() -> Unit)? = null,
    titleTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    messageTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier.fillMaxSize().background(backgroundColor).padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(thumbnailResId),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(spacing.medium)
                    .sizeIn(
                        minWidth = 120.dp,
                        minHeight = 120.dp,
                        maxWidth = 400.dp,
                        maxHeight = 400.dp
                    )
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )
            Text(
                text = title,
                style = titleTextStyle,
                textAlign = TextAlign.Center,
                color = textColor,
            )
            message?.let {
                Spacer(modifier = Modifier.height(spacing.medium))
                Text(
                    text = message,
                    style = messageTextStyle,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
            Spacer(modifier = modifier.height(spacing.medium))
        }
       actionText?.let {
           Spacer(modifier = Modifier.height(spacing.small))
           Button(
               modifier = Modifier.fillMaxWidth(),
               onClick = actionClick ?: {}
           ) {
               Text(
                   text = it,
                   modifier = Modifier.padding(spacing.small),
                   style = MaterialTheme.typography.titleMedium
               )
           }
       }
    }

}

@Preview(showBackground = true)
@Composable
private fun NoDataScreenPreview() {
    ChronarchTheme {
        NoDataScreen(
            thumbnailResId = R.drawable.app_error,
            contentDescription = "No sessions found",
            title = "Complete sessions to see the history here",
            actionText = "Retry"
        )
    }
}