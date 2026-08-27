package com.aashish.chronarch.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.chronarch.common.ui.theme.ChronarchTheme

@Composable
fun VerticalLabelledValue(
    value: String,
    label: String,
    valueTextStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    labelTextStyle: TextStyle = MaterialTheme.typography.labelSmall,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = value, style = valueTextStyle)
        Text(text = label, style = labelTextStyle)
    }
}

@Preview(showBackground = true)
@Composable
private fun VerticalLabelledValuePreview() {
    ChronarchTheme {
        VerticalLabelledValue("3", "Completed")
    }
}