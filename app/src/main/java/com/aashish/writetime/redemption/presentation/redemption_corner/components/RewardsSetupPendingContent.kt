package com.aashish.writetime.redemption.presentation.redemption_corner.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun RewardsSetupPendingContent(
    onProceedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.setup),
                contentDescription = stringResource(R.string.action_setup_image),
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
                text = stringResource(R.string.setup_actions_message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(spacing.medium)
            )
            Text(
                text = stringResource(R.string.note_one_time_action),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontStyle = FontStyle.Italic
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(spacing.medium),
            onClick = onProceedClick
        ) {
            Text(
                text = stringResource(R.string.proceed),
                modifier = Modifier.padding(spacing.small),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@PreviewScreenSizes
@Composable
private fun RewardsSetupPendingContentPreview() {
    WriteTimeTheme {
        RewardsSetupPendingContent({})
    }

}