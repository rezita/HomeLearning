package com.github.rezita.homelearning.ui.screens.uploadwords

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.screens.uploadwords.edit.EditScreen
import com.github.rezita.homelearning.ui.viewmodels.UploadWordViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun UploadWordsScreen(
    sheetAction: SheetAction,
    viewModel: UploadWordViewModel = viewModel(
        factory = UploadWordViewModel.UploadWordViewModelFactory(
            sheetAction
        )
    ),
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uploadUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            UploadWordsTopAppBar(
                state = uploadUiState,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                saveCallback = { viewModel.saveSpellingWords() },
                addNewCallback = { viewModel.setForEditing(null) }
            )
        }
    ) {
        UploadContent(
            state = uploadUiState,
            viewModel = viewModel,
            scope = scope,
            snackBarHostState = snackBarHostState,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
private fun UploadContent(
    state: UploadUiState,
    viewModel: UploadWordViewModel,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    when (state) {
        is UploadUiState.Loading -> LoadingProgressBar(modifier = modifier)
        is UploadUiState.Saving -> LoadingProgressBar(modifier = modifier)
        is UploadUiState.NoWords -> NoWordsContent(modifier = modifier)

        is UploadUiState.LoadingError -> {
            LoadingErrorSnackbar(
                scope = scope,
                snackbarHostState = snackBarHostState
            )
            ErrorDisplayInColumn(
                message = stringResource(id = state.errorMessage!!),
                callback = { viewModel.initCategories() },
                modifier = modifier
            )
        }

        is UploadUiState.SavingError -> {
            SavingErrorSnackbar(
                scope = scope,
                snackbarHostState = snackBarHostState
            )
            ErrorDisplayWithContent(
                message = stringResource(id = state.errorMessage!!),
                callback = { viewModel.saveSpellingWords() },
                content = {
                    UploadWordsSaveErrorContent(
                        state = state,
                        modifier = modifier
                    )
                },
                modifier = modifier
            )

        }

        is UploadUiState.Editing -> {
            EditScreen(
                state = state,
                saveCallback = { viewModel.updateWords() },
                cancelCallback = { viewModel.cancelUpdate() },
                onWordChangeCallback = { value -> viewModel.updateCurrentWordWord(value) },
                onCommentChangeCallback = { value ->
                    viewModel.updateCurrentWordComment(
                        value
                    )
                },
                onCategoryChangeCallback = { value ->
                    viewModel.updateCurrentWordCategory(
                        value
                    )
                },
                modifier = modifier
            )
        }

        is UploadUiState.HasWords ->
            UploadWordsViewContent(
                state = state,
                onWordEdit = { index -> viewModel.setForEditing(index) },
                onWordDelete = { index -> viewModel.removeWord(index) },
                modifier = modifier
            )

        is UploadUiState.Saved -> {
            SavingSuccessSnackbar(
                scope = scope,
                snackbarHostState = snackBarHostState
            )
            UploadWordsSavedContent(state = state, modifier = modifier)
        }

    }
}

@Composable
private fun NoWordsContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
        Text(text = stringResource(id = R.string.upload_no_words_message))
    }
}

