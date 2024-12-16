package com.github.rezita.homelearning.ui.screens.uploadwords

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.rezita.homelearning.model.SpellingWord
import kotlinx.coroutines.CoroutineScope

@Composable
fun UploadWordsScreen(
    state: UploadUiState,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (UploadWordUserEvent) -> Unit,
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
                onUserEvent = onUserEvent
            )
        }
    ) {
        UploadWordsContent(
            state = state,
            scope = scope,
            snackBarHostState = snackBarHostState,
            onUserEvent = onUserEvent,
            modifier = modifier.padding(it)
        )
    }

}