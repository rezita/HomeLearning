package com.github.rezita.homelearning.ui.screens.uploadwords

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.rezita.homelearning.ui.viewmodels.UploadWordViewModel

@Composable
fun UploadWordsRoute(
    viewModel: UploadWordViewModel,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uploadUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    UploadWordsScreen(
        state = uploadUiState,
        onLoadCallback = viewModel::initCategories,
        saveCallback = viewModel::saveSpellingWords,
        onWordEditCallback = viewModel::setForEditing,
        onRemoveWordCallback = viewModel::removeWord,
        onWordSaveCallback = viewModel::updateWords,
        onCancelEditCallback = viewModel::cancelUpdate,
        onWordChangeCallback = viewModel::updateCurrentWord,
        scope = scope,
        snackBarHostState = snackBarHostState,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
        modifier = modifier
    )
}
