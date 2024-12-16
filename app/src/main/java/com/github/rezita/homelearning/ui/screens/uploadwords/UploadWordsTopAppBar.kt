package com.github.rezita.homelearning.ui.screens.uploadwords

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
import com.github.rezita.homelearning.ui.screens.uploadwords.edit.EditState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun UploadWordsTopAppBar(
    state: UploadUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (UploadWordUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        title =
        when (state) {
            is UploadUiState.Editing -> ("")
            else -> stringResource(id = R.string.activity_upload_words)

        },
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            if (state.isSavable()) {
                IconButton(onClick = { onUserEvent(UploadWordUserEvent.OnSave) }) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_result),
                        contentDescription = stringResource(id = R.string.upload_saving_words),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            if (state.isExpandable()) {
                IconButton(onClick = { onUserEvent(UploadWordUserEvent.OnAddNew) }) {
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
        UploadWordsTopAppBar(
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
        UploadWordsTopAppBar(
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
        UploadWordsTopAppBar(
            state = UploadUiState.Editing(EditState(), emptyList()),
            canNavigateBack = true,
            navigateUp = {},
            onUserEvent = {},
        )
    }
}