package com.github.rezita.homelearning.ui.screens.spanish

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpanishScreen(
    state: SpanishUiState,
    windowSize: HomeLearningWindowSizeClass,
    orientation: Int,
    action: SheetAction,
    titleId: Int,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (SpanishUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.navigationBarsPadding(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            if (action != SheetAction.READ_SPANISH_WORDS) {
                SpanishTopAppBar(
                    titleId = titleId,
                    state = state,
                    action = action,
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateUp,
                    onUserEvent = onUserEvent
                )
            }
        }
    ) {
        SpanishContent(
            state = state,
            action = action,
            windowSize = windowSize,
            orientation = orientation,
            scope = scope,
            snackBarHostState = snackBarHostState,
            onUserEvent = onUserEvent,
            navigateUp = navigateUp,
            modifier = modifier.padding(it)
        )
    }
}