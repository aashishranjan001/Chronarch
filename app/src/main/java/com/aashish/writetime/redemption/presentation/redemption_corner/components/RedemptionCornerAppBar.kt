package com.aashish.writetime.redemption.presentation.redemption_corner.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedemptionCornerTopAppBar(
    title: String, balance: Int, modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier, title = {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
        },
        windowInsets = WindowInsets(),
        actions = {
            FocusPointCurrency(value = balance, modifier = Modifier.padding(LocalSpacing.current.medium))
        })

}

@Preview
@Composable
fun RedemptionCornerTopAppBarPreview() {
    WriteTimeTheme {
        RedemptionCornerTopAppBar("Redemption Corner", 3009)
    }
}