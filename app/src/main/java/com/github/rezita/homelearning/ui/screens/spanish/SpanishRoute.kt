package com.github.rezita.homelearning.ui.screens.spanish

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.viewmodels.AppViewModelProvider
import com.github.rezita.homelearning.ui.viewmodels.SpanishViewModel

@Composable
fun SpanishRoute(
    @StringRes titleId: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    windowSize: HomeLearningWindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: SpanishViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val action = viewModel.sheetAction
    val showTranslate = viewModel.showTranslate
    val spanishUiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    SpanishScreen(
        state = spanishUiState,
        windowSize = windowSize,
        orientation = configuration.orientation,
        action = action,
        titleId = titleId,
        showTranslate = showTranslate,
        scope = scope,
        snackBarHostState = snackBarHostState,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
        onUserEvent = viewModel::onEvent,
        modifier = modifier,
    )
}