package com.aashish.chronarch.history.presentation.model

data class FilterCategoryItemUiState(
    val category: FilterCategory,
    val showSelected: Boolean = false,
    val hasAnyFilterOptionSelected: Boolean
)