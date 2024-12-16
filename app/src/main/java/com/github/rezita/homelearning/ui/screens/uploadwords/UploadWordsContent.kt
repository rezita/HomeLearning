package com.github.rezita.homelearning.ui.screens.uploadwords

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.screens.uploadwords.component.UploadWordsSaveErrorContent
import com.github.rezita.homelearning.ui.screens.uploadwords.component.UploadWordsSavedContent
import com.github.rezita.homelearning.ui.screens.uploadwords.component.UploadWordsViewContent
import com.github.rezita.homelearning.ui.screens.uploadwords.edit.EditScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun UploadWordsContent(
    state: UploadUiState,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onUserEvent: (UploadWordUserEvent) -> Unit,
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
                callback = { onUserEvent(UploadWordUserEvent.OnLoad) },
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
                callback = { onUserEvent(UploadWordUserEvent.OnSave) },
                content = {
                    UploadWordsSaveErrorContent(
                        state = state
                    )
                },
                modifier = modifier
            )

        }

        is UploadUiState.Editing -> {
            EditScreen(
                state = state,
                onUserEvent = onUserEvent,
                modifier = modifier
            )
        }

        is UploadUiState.HasWords ->
            UploadWordsViewContent(
                state = state,
                onUserEvent = onUserEvent,
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

