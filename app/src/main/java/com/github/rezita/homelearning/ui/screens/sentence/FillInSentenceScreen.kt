package com.github.rezita.homelearning.ui.screens.sentence

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope


@Composable
fun FillInSentenceScreen(
    state: SentenceUiState,
    titleId: Int,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (SentenceUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.navigationBarsPadding(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            SentenceTopAppBar(
                titleId = titleId,
                state = state,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                onUserEvent = onUserEvent
            )
        }
    ) {
        SentenceContent(
            state = state,
            scope = scope,
            snackBarHostState = snackBarHostState,
            onUserEvent = onUserEvent,
            modifier = Modifier
                .padding(it)
        )
    }
}