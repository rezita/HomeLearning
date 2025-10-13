package com.github.rezita.homelearning.ui.screens.spellingupload

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.screens.spellingupload.edit.EditState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpellingUploadTopAppBar(
    state: SpellingUploadUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (SpellingUploadUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        title =
        when (state) {
            is SpellingUploadUiState.Editing -> ("")
            else -> stringResource(id = R.string.activity_upload_words)

        },
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            if (state.isSavable()) {
                IconButton(onClick = { onUserEvent(SpellingUploadUserEvent.OnSave) }) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_result),
                        contentDescription = stringResource(id = R.string.upload_saving_words),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            if (state.isExpandable()) {
                IconButton(onClick = { onUserEvent(SpellingUploadUserEvent.OnAddNew) }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_add),
                        contentDescription = stringResource(id = R.string.upload_add_new),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun UploadWordsTopAppBar_downloaded() {
    HomeLearningTheme {
        SpellingUploadTopAppBar(
            state = SpellingUploadUiState.NoWords,
            canNavigateBack = true,
            navigateUp = {},
            onUserEvent = {},
        )
    }
}

@Preview
@Composable
private fun UploadWordsTopAppBar_hasWords() {
    HomeLearningTheme {
        SpellingUploadTopAppBar(
            state = SpellingUploadUiState.HasWords(words = emptyList()),
            canNavigateBack = true,
            navigateUp = {},
            onUserEvent = {},
        )
    }
}

@Preview
@Composable
private fun UploadWordsTopAppBar_editing() {
    HomeLearningTheme {
        SpellingUploadTopAppBar(
            state = SpellingUploadUiState.Editing(EditState(), emptyList()),
            canNavigateBack = true,
            navigateUp = {},
            onUserEvent = {},
        )
    }
}