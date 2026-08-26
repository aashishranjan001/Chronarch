package com.aashish.writetime.weekoverview.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@Composable
fun FocusPointsTransactionsBreakdown(
    taskCredits: Int,
    bonusPoints: Int,
    redemptionPoints: Int,
    totalFocusPoints: Int,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = spacing.extraSmall
        ),
        modifier = modifier, shape = RoundedCornerShape(spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(
                space = spacing.medium,
                alignment = Alignment.CenterVertically
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.task_credit_focus_points),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(text = taskCredits.toString(), style = MaterialTheme.typography.titleMedium)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.bonus_points),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(text = bonusPoints.toString(), style = MaterialTheme.typography.titleMedium)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.redemption_points),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = redemptionPoints.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.total_focus_points),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = totalFocusPoints.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}

@Preview
@Composable
private fun FocusPointsTransactionsBreakdownPreview() {
    WriteTimeTheme {
        FocusPointsTransactionsBreakdown(10, 3, 2, 3)
    }

}