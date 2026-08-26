package com.aashish.writetime.redemption.presentation.redemption_corner.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun FocusPointCurrency(
    value: Int,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .clip(shape = CircleShape)
    ) {
        Icon(
            modifier = Modifier.padding(
                start = spacing.small,
                end = 0.dp,
                top = spacing.small,
                bottom = spacing.small
            ),
            imageVector = Icons.Default.Diamond,
            tint = contentColor,
            contentDescription = stringResource(R.string.rewards_icon)
        )
        Spacer(modifier = Modifier.width(spacing.extraSmall))
        Text(
            text = value.toString(),
            color = contentColor,
            modifier = Modifier.padding(
                start = 0.dp,
                end = spacing.small,
                top = spacing.small,
                bottom = spacing.small
            )
        )
    }
}

@Preview
@Composable
private fun FocusPointCurrencyPreview() {
    WriteTimeTheme {
        FocusPointCurrency(value = 12)
    }
}