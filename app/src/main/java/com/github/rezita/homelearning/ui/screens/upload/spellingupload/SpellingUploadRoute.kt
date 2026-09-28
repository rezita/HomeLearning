package com.github.rezita.homelearning.ui.screens.upload.spellingupload

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.ui.viewmodels.AppViewModelProvider
import com.github.rezita.homelearning.ui.viewmodels.SpellingUploadViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpellingUploadRoute(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: SpellingUploadViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uploadUiState by viewModel.uiState.collectAsStateWithLifecycle()

    SpellingUploadScreen(
        state = uploadUiState,
        scope = scope,
        snackBarHostState = snackBarHostState,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
        onUserEvent = viewModel::onUserEvent,
        modifier = modifier
    )
}
