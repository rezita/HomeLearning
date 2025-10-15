package com.github.rezita.homelearning.ui.screens.upload.common.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.Uploadable
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun <T : Uploadable> UploadTopAppBar(
    state: UploadUiState<T>,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (UploadUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        title = stringResource(id = R.string.activity_upload_words),
        canNavigateBack = canNavigateBack && state.canNavBack(),
        navigateUp = navigateUp,

        actions = {
            if (state.isSavable()) {
                IconButton(onClick = { onUserEvent(UploadUserEvent.OnSave) }) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_result),
                        contentDescription = stringResource(id = R.string.upload_saving_words),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            if (state.isExpandable()) {
                IconButton(onClick = { onUserEvent(UploadUserEvent.OnAddNew) }) {
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
        UploadTopAppBar(
            state = UploadUiState.NoWords,
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
        UploadTopAppBar(
            state = UploadUiState.HasWords(words = emptyList()),
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
        UploadTopAppBar(
            state = UploadUiState.Editing(EditState(SpellingWord("", "", "")), emptyList()),
            canNavigateBack = true,
            navigateUp = {},
            onUserEvent = {},
        )
    }
}