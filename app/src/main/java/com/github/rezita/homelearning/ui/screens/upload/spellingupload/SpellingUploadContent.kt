package com.github.rezita.homelearning.ui.screens.upload.spellingupload

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.screens.upload.common.SpellingUploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.components.NoWordsContent
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadSaveErrorContent
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadSavedContent
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadViewContent
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditScreen
import com.github.rezita.homelearning.ui.screens.upload.spellingupload.component.SpellingSavedItem
import com.github.rezita.homelearning.ui.screens.upload.spellingupload.component.SpellingUploadItem
import com.github.rezita.homelearning.ui.screens.upload.spellingupload.component.SpellingUploadViewItem
import com.github.rezita.homelearning.ui.screens.upload.spellingupload.component.SpellingWordEditForm
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpellingUploadContent(
    state: UploadUiState<SpellingWord>,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onUserEvent: (SpellingUploadUserEvent) -> Unit,
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
                callback = { onUserEvent(SpellingUploadUserEvent.OnLoad) },
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
                callback = { onUserEvent(UploadUserEvent.OnSave) },
                content = {
                    UploadSaveErrorContent(
                        state = state,
                        modifier = modifier
                    ) { SpellingUploadItem(it) }
                },
                modifier = modifier
            )

        }

        is UploadUiState.Editing -> {
            EditScreen(
                content = {
                    SpellingWordEditForm(
                        state = state,
                        onUserEvent = onUserEvent
                    )
                },
                modifier = modifier,
                saveCallback = { onUserEvent(UploadUserEvent.OnSaveEditedWord) },
                cancelCallback = { onUserEvent(UploadUserEvent.OnCancelEditing) },
            )
        }

        is UploadUiState.HasWords ->
            UploadViewContent(
                state = state,
                modifier = modifier
            ) { index, item ->
                SpellingUploadViewItem(
                    word = item,
                    onDeleteCallback = { onUserEvent(UploadUserEvent.OnRemoveWord(index)) },
                    onEditCallback = { onUserEvent(UploadUserEvent.OnPrepareForEditing(index)) },
                )
            }

        is UploadUiState.Saved -> {
            SavingSuccessSnackbar(
                scope = scope,
                snackbarHostState = snackBarHostState
            )
            UploadSavedContent(
                state = state,
                modifier = modifier
            ) { SpellingSavedItem(it) }

        }
    }
}