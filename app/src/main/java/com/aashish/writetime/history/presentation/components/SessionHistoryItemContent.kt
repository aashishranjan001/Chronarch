package com.aashish.writetime.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.components.IconText
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun SessionHistoryItemContent(
    targetDuration: String,
    runDuration: String,
    startTime: String,
    endTime: String,
    date: String,
    percentageCompleted: Double,
    modifier: Modifier = Modifier) {

    val spacing = LocalSpacing.current
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(spacing.large)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                IconText(
                    icon = ImageVector.vectorResource(R.drawable.datetime),
                    contentDescription = stringResource(R.string.timer_runtime),
                    text = stringResource(R.string.runtime_start_to_end, date, startTime, endTime)
                )
                IconText(
                    icon = ImageVector.vectorResource(R.drawable.goal),
                    contentDescription = stringResource(R.string.goal_duration),
                    text = targetDuration
                )
            }
            Spacer(modifier = Modifier.width(spacing.small))
            CircularProgressIndicator(runDuration, percentageCompleted)
        }
    }
}

@Preview
@Composable
private fun SessionHistoryItemContentPreview() {
    WriteTimeTheme {
        SessionHistoryItemContent(
            targetDuration = "60 min",
            runDuration = "8 min",
            date = "21 Aug",
            startTime = "1:40am",
            endTime = "1:48am",
            percentageCompleted = 67.0
        )
    }
}