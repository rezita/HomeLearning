package com.github.rezita.homelearning.ui.screens.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.rezita.homelearning.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun SavingErrorSnackbar(
    scope: CoroutineScope, snackbarHostState: SnackbarHostState
) {
    HomeLearningSnackbar(
        scope = scope,
        snackbarHostState = snackbarHostState,
        message = stringResource(id = R.string.snackBar_save_error)
    )
}

@Composable
fun LoadingErrorSnackbar(
    scope: CoroutineScope, snackbarHostState: SnackbarHostState
) {
    HomeLearningSnackbar(
        scope = scope,
        snackbarHostState = snackbarHostState,
        message = stringResource(id = R.string.snackBar_error_loading)
    )
}

@Composable
fun SavingSuccessSnackbar(
    scope: CoroutineScope, snackbarHostState: SnackbarHostState
) {
    HomeLearningSnackbar(
        scope = scope,
        snackbarHostState = snackbarHostState,
        message = stringResource(id = R.string.snackBar_save_success)
    )
}

@Composable
private fun HomeLearningSnackbar(
    scope: CoroutineScope, snackbarHostState: SnackbarHostState,
    message: String, duration: SnackbarDuration = SnackbarDuration.Short
) {
    scope.launch {
        snackbarHostState.showSnackbar(
            message = message,
            duration = duration,
            withDismissAction = true
        )
    }
}
