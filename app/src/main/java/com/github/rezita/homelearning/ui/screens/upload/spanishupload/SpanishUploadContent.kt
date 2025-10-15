package com.github.rezita.homelearning.ui.screens.upload.spanishupload

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.screens.upload.common.SpanishUploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.components.NoWordsContent
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadSaveErrorContent
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadSavedContent
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadViewContent
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditScreen
import com.github.rezita.homelearning.ui.screens.upload.spanishupload.components.SpanishSavedItem
import com.github.rezita.homelearning.ui.screens.upload.spanishupload.components.SpanishUploadWordItem
import com.github.rezita.homelearning.ui.screens.upload.spanishupload.components.SpanishUploadViewItem
import com.github.rezita.homelearning.ui.screens.upload.spanishupload.components.SpanishWordEditForm
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpanishUploadContent(
    state: UploadUiState<SpanishWord>,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onUserEvent: (SpanishUploadUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        //this cannot be happen
        is UploadUiState.Loading -> LoadingProgressBar(modifier = modifier)
        //this cannot be happen
        is UploadUiState.LoadingError -> {}
        is UploadUiState.Saving -> LoadingProgressBar(modifier = modifier)
        is UploadUiState.NoWords -> NoWordsContent(modifier = modifier)

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
                    ) { SpanishUploadWordItem(it) }
                },
                modifier = modifier
            )
        }

        is UploadUiState.Editing -> {
            EditScreen(
                content = {
                    SpanishWordEditForm(
                        state = state,
                        onUserEvent = onUserEvent,
                    )
                },
                modifier = modifier,
                saveCallback = { onUserEvent(UploadUserEvent.OnSaveEditedWord) },
                cancelCallback = { onUserEvent(UploadUserEvent.OnCancelEditing) },
            )
        }

        is UploadUiState.HasWords -> {
            UploadViewContent(
                state = state,
                modifier = modifier
            ) { index, item ->
                SpanishUploadViewItem(
                    word = item,
                    onDeleteCallback = { onUserEvent(UploadUserEvent.OnRemoveWord(index)) },
                    onEditCallback = { onUserEvent(UploadUserEvent.OnPrepareForEditing(index)) },
                )
            }
        }

        is UploadUiState.Saved -> {
            SavingSuccessSnackbar(
                scope = scope,
                snackbarHostState = snackBarHostState
            )
            UploadSavedContent(
                state = state,
                modifier = modifier
            ) { wordResult -> SpanishSavedItem(wordResult) }
        }
    }
}