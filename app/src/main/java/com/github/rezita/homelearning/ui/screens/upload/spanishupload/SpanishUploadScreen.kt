package com.github.rezita.homelearning.ui.screens.upload.spanishupload

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.ui.screens.upload.common.SpanishUploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadTopAppBar
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpanishUploadScreen(
    state: UploadUiState<SpanishWord>,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (SpanishUploadUserEvent) -> Unit,
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
        SpanishUploadContent(
            state = state,
            scope = scope,
            snackBarHostState = snackBarHostState,
            onUserEvent = onUserEvent,
            modifier = modifier.padding(it)
        )
    }
}