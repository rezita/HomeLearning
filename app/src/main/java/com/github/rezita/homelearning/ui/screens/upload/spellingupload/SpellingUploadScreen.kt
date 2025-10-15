package com.github.rezita.homelearning.ui.screens.upload.spellingupload

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.screens.upload.common.SpellingUploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadTopAppBar
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpellingUploadScreen(
    state: UploadUiState<SpellingWord>,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (SpellingUploadUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            UploadTopAppBar(
                state = state,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                onUserEvent = onUserEvent
            )
        }
    ) {
        SpellingUploadContent(
            state = state,
            scope = scope,
            snackBarHostState = snackBarHostState,
            onUserEvent = onUserEvent,
            modifier = modifier.padding(it)
        )
    }

}