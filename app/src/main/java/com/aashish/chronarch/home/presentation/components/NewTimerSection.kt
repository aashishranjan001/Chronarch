package com.aashish.chronarch.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.chronarch.R
import com.aashish.chronarch.common.domain.model.DurationType
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.theme.ChronarchTheme

@Composable
fun NewTimerSection(
    selectedTimerType: DurationType,
    onTimerTypeSelected: (DurationType) -> Unit,
    onStartTimerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = spacing.extraSmall
        ),
        shape = RoundedCornerShape(spacing.medium),
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.complete_sessions_earn_reward),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.large)
                ) {
                    SegmentedButton(
                        selected = selectedTimerType == DurationType.ShortDuration,
                        onClick = {
                            onTimerTypeSelected(DurationType.ShortDuration)
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 0,
                            count = 2
                        ),
                        label = {
                            Text(
                                text = stringResource(
                                    R.string.short_duration,
                                    DurationType.ShortDuration.duration.inWholeMinutes,
                                )
                            )
                        },
                    )
                    SegmentedButton(
                        selected = selectedTimerType == DurationType.LongDuration,
                        onClick = {
                            onTimerTypeSelected(DurationType.LongDuration)
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 1,
                            count = 2
                        ),
                        label = {
                            Text(
                                text = stringResource(
                                    R.string.long_duration,
                                    DurationType.LongDuration.duration.inWholeMinutes
                                )
                            )
                        }

                    )
                }

            }
            Spacer(modifier = Modifier.height(spacing.small))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onStartTimerClick
            ) {
                Text(
                    text = stringResource(R.string.start_timer),
                    modifier = Modifier.padding(spacing.small),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewTimerSectionPreview() {
    ChronarchTheme {
        NewTimerSection(
            selectedTimerType = DurationType.ShortDuration, {}, {})
    }
}