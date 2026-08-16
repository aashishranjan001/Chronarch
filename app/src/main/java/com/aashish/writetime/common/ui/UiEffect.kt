package com.aashish.writetime.common.ui

sealed class UiEffect {
    data class ShowSnackbar(val message: String): UiEffect()
    data object GoBack: UiEffect()
}