package com.aashish.writetime.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.VerticalLabelledValue
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun SessionsCard(
    totalSession: Int,
    completedSessions: Int,
    cancelledSessions: Int,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(spacing.medium))
            .background(
                MaterialTheme.colorScheme.primary
            )
            .padding(spacing.medium),
        verticalArrangement = Arrangement.Center

    ) {
        Text(text = stringResource(R.string.sessions), style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(modifier = Modifier.height(spacing.small))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            VerticalLabelledValue(
                value = totalSession.toString(),
                label = stringResource(R.string.started),
                modifier = Modifier.weight(1f)
            )
            VerticalLabelledValue(
                value = completedSessions.toString(),
                label = stringResource(R.string.completed),
                modifier = Modifier.weight(1f)
            )
            VerticalLabelledValue(
                value = cancelledSessions.toString(),
                label = stringResource(R.string.cancelled),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun SessionsCardPreview() {
    WriteTimeTheme {
        SessionsCard(10, 7, 3)
    }
}