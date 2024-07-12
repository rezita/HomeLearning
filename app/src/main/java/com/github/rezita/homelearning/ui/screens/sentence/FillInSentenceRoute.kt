package com.github.rezita.homelearning.ui.screens.sentence

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.ui.viewmodels.AppViewModelProvider
import com.github.rezita.homelearning.ui.viewmodels.FillInSentenceViewModel

@Composable
fun FillInSentenceSentenceRoute(
    @StringRes titleId: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FillInSentenceViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val sentenceUiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    FillInSentenceScreen(
        state = sentenceUiState,
        onValueChange = viewModel::updateAnswer,
        onDoneCallback = viewModel::saveSentences,
        onLoadCallback = viewModel::load,
        scope = scope,
        snackBarHostState = snackBarHostState,
        titleId = titleId,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
        modifier = modifier
    )
}