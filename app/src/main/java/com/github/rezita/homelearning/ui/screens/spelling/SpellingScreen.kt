package com.github.rezita.homelearning.ui.screens.spelling

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.rezita.homelearning.model.WordStatus
import kotlinx.coroutines.CoroutineScope


@Composable
fun SpellingScreen(
    state: SpellingUiState,
    onItemValueChange: (Int, WordStatus) -> Unit,
    onItemReset: (Int) -> Unit,
    onItemEdit: (Int) -> Unit,
    onEditItemValueChange: (String) -> Unit,
    onLoadCallback: () -> Unit,
    onSaveCallback: () -> Unit,
    rbContentType: RadioButtonContentType,
    addNewCallback: () -> Unit,
    saveCallback: () -> Unit,
    onEditCancelCallback: () -> Unit,
    onEditSubmitCallback: () -> Unit,
    onSpeakerClick: (String) -> Unit,
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
            SpellingTopAppBar(
                state = state,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                saveCallback = saveCallback,
                addNewCallback = addNewCallback,
                redoCallback = onLoadCallback
            )
        }
    ) {
        SpellingContent(
            state = state,
            onItemValueChange = onItemValueChange,
            onItemReset = onItemReset,
            onItemEdit = onItemEdit,
            onEditItemValueChange = onEditItemValueChange,
            onLoadCallback = onLoadCallback,
            onSaveCallback = onSaveCallback,
            onEditCancelCallback = onEditCancelCallback,
            onEditSubmitCallback = onEditSubmitCallback,
            onSpeakerClick = onSpeakerClick,
            scope = scope,
            snackBarHostState = snackBarHostState,
            rbContentType = rbContentType,
            modifier = Modifier.padding(it)
        )
    }
}