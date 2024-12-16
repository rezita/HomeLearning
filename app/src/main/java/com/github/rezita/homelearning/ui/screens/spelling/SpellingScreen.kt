package com.github.rezita.homelearning.ui.screens.spelling

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope


@Composable
fun SpellingScreen(
    state: SpellingUiState,
    rbContentType: RadioButtonContentType,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    addNewCallback: () -> Unit,
    onUserEvent: (SpellingUserEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            SpellingTopAppBar(
                state = state,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                addNewCallback = addNewCallback,
                onUserEvent = onUserEvent
            )
        }
    ) {
        SpellingContent(
            state = state,
            rbContentType = rbContentType,
            scope = scope,
            snackBarHostState = snackBarHostState,
            onUserEvent = onUserEvent,
            modifier = Modifier.padding(it)
        )
    }
}