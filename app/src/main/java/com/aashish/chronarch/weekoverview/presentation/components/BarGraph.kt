package com.aashish.chronarch.weekoverview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.chronarch.common.ui.LocalSpacing
import com.aashish.chronarch.common.ui.theme.ChronarchTheme
import com.aashish.chronarch.common.ui.toMax1DecimalPlace

@Composable
fun BarGraph(
    valuesMap: Map<String, Int>,
    labelTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    barColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current
    val maxValue: Float = valuesMap.values.maxOfOrNull { it.toFloat() * 1.2f } ?: 0f
    val yAxisLabels = buildList {
        if (maxValue != 0f) {
            for (i in 1..4) {
                add(maxValue / i)
            }
            add(0f)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(spacing.medium))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(spacing.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Column(
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                yAxisLabels.forEach { label ->
                    Text(
                        text = label.toDouble().toMax1DecimalPlace(),
                        modifier = Modifier.padding(end = spacing.medium),
                        style = labelTextStyle
                    )
                }
            }
            VerticalDivider(modifier = Modifier.width(4.dp))
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.Bottom) {
                valuesMap.values.forEach { yValue ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(Color.Transparent)
                            .weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(yValue.toFloat() / maxValue)
                            .clip(
                                RoundedCornerShape(
                                    topStart = spacing.medium,
                                    topEnd = spacing.medium
                                )
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        barColor.copy(alpha = 0.7f),
                                        barColor
                                    )
                                )
                            )
                            .weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(Color.Transparent)
                            .weight(1f)
                    )
                }
            }

        }
        HorizontalDivider(
            modifier = Modifier
                .height(4.dp)
                .padding(start = 40.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 40.dp)
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            valuesMap.keys.forEach { xAxisLabel ->
                Text(
                    text = xAxisLabel,
                    modifier = Modifier
                        .padding(top = spacing.small)
                        .weight(1f),
                    style = labelTextStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun BarGraphPreview() {
    ChronarchTheme {
        BarGraph(
            valuesMap = mapOf(
                "Mon" to 22,
                "Tue" to 33,
                "Wed" to 18,
                "Thu" to 0,
                "Fri" to 40
            )
        )
    }
}