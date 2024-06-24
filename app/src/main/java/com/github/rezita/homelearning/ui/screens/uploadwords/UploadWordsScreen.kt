package com.github.rezita.homelearning.ui.screens.uploadwords

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope

@Composable
fun UploadWordsScreen(
    state: UploadUiState,
    onLoadCallback: () -> Unit,
    saveCallback: () -> Unit,
    addNewCallback: () -> Unit,
    onWordEditCallback: (Int) -> Unit,
    onRemoveWordCallback: (Int) -> Unit,
    onWordSaveCallback: () -> Unit,
    onCancelEditCallback: () -> Unit,
    onWordChangeCallback: (String) -> Unit,
    onCommentChangeCallback: (String) -> Unit,
    onCategoryChangeCallback: (String) -> Unit,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            UploadWordsTopAppBar(
                state = state,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                saveCallback = saveCallback,
                addNewCallback = addNewCallback
            )
        }
    ) {
        UploadWordsContent(
            state = state,
            onLoadCallback = onLoadCallback,
            onSaveCallback = saveCallback,
            onWordEditCallback = onWordEditCallback,
            onRemoveWordCallback = onRemoveWordCallback,
            onWordSaveCallback = onWordSaveCallback,
            onCancelEditCallback = onCancelEditCallback,
            onWordChangeCallback = onWordChangeCallback,
            onCommentChangeCallback = onCommentChangeCallback,
            onCategoryChangeCallback = onCategoryChangeCallback,
            scope = scope,
            snackBarHostState = snackBarHostState,
            modifier = modifier.padding(it)
        )
    }

}