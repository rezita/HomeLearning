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
    onValueChange: (Int, String) -> Unit,
    onDoneCallback: () -> Unit,
    onLoadCallback: () -> Unit,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    titleId: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
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
                callback = onDoneCallback
            )
        }
    ) {
        SentenceContent(
            state = state,
            onValueChange = onValueChange,
            onDoneCallback = onDoneCallback,
            onLoadCallback = onLoadCallback,
            //viewModel = viewModel,
            scope = scope,
            snackBarHostState = snackBarHostState,
            modifier = Modifier
                .padding(it)
        )
    }
}