package com.aashish.chronarch.redemption.presentation.rewards_setup.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aashish.chronarch.R
import com.aashish.chronarch.common.ui.theme.ChronarchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupRewardActionsAppBar(
    isSaveEnabled: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    title: String, modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier, title = {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
        },
        windowInsets = WindowInsets(),
        navigationIcon = {
            IconButton(
                onClick = onCancel
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.cancel))
            }
        },
        actions = {
            IconButton(
                onClick = onSave,
                enabled = isSaveEnabled
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(R.string.save_rewards))
            }
        })

}

@Preview
@Composable
fun SetupRewardActionsAppBarPreview() {
    ChronarchTheme {
        SetupRewardActionsAppBar(true, {}, {}, "Setup Reward Actions")
    }
}