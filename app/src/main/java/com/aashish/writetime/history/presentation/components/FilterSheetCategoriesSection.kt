package com.aashish.writetime.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aashish.writetime.R
import com.aashish.writetime.common.ui.LocalSpacing
import com.aashish.writetime.common.ui.theme.WriteTimeTheme
import com.aashish.writetime.history.presentation.model.FilterCategory
import com.aashish.writetime.history.presentation.model.FilterCategoryItemUiState

@Composable
fun FilterSheetCategoriesSection(
    categories: List<FilterCategoryItemUiState>,
    onCategoryClick: (FilterCategory) -> Unit,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        categories.forEach { categoryState ->
            item {
                Row(
                    modifier = Modifier
                        .heightIn(max = 80.dp)
                        .clickable {
                            onCategoryClick(categoryState.category)
                        }
                        .fillMaxWidth()
                        .background(if (categoryState.showSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                        .padding(spacing.medium)
                    ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (categoryState.hasAnyFilterOptionSelected) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.secondary
                                )
                        )
                        Spacer(modifier = Modifier.width(spacing.small))
                    }
                    val labelRes = when (categoryState.category) {
                        FilterCategory.SessionFilter.CompletionStatus -> R.string.category_completion_status
                        FilterCategory.Date -> R.string.category_date
                        FilterCategory.SessionFilter.DurationType -> R.string.category_duration_length
                        FilterCategory.TransactionFilter.Type -> R.string.category_transaction_type
                    }
                    Text(
                        text = stringResource(labelRes),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FilterSheetCategoriesSectionPreview() {
    WriteTimeTheme {
        FilterSheetCategoriesSection(
            categories = listOf(
                FilterCategoryItemUiState(FilterCategory.SessionFilter.DurationType, false, true),
                FilterCategoryItemUiState(FilterCategory.SessionFilter.CompletionStatus, true, false),
                FilterCategoryItemUiState(FilterCategory.Date, false, false),
            ),
            {}
        )
    }
}