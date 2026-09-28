package com.github.rezita.homelearning.ui.screens.sentence

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.ui.viewmodels.AppViewModelProvider
import com.github.rezita.homelearning.ui.viewmodels.FillInSentenceViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun FillInSentenceSentenceRoute(
    @StringRes titleId: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: FillInSentenceViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val sentenceUiState by viewModel.uiState.collectAsState()

    FillInSentenceScreen(
        state = sentenceUiState,
        titleId = titleId,
        scope = scope,
        snackBarHostState = snackBarHostState,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
        onUserEvent = viewModel::onEvent,
        modifier = modifier
    )
}