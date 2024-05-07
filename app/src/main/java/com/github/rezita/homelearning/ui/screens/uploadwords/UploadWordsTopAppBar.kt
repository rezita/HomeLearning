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
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun UploadWordsTopAppBar(
    state: UploadUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    saveCallback: () -> Unit,
    addNewCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        titleText =
        when (state) {
            is UploadUiState.Loading -> stringResource(id = R.string.app_bar_loading_title)
            is UploadUiState.Saving -> stringResource(id = R.string.app_bar_uploading_title)
            is UploadUiState.SavingError -> stringResource(id = R.string.app_bar_error_title)
            is UploadUiState.LoadingError -> stringResource(id = R.string.app_bar_error_title)
            is UploadUiState.Editing -> ("")
            else -> stringResource(id = R.string.activity_upload_words)

        },
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            if (state.isSavable) {
                IconButton(onClick = saveCallback) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_result),
                        contentDescription = stringResource(id = R.string.spelling_save),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            if (state.isExpandable) {
                IconButton(onClick = { addNewCallback() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_add),
                        contentDescription = stringResource(id = R.string.spelling_add_new_word),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun UploadWordsTopAppBar_downloaded() {
    HomeLearningTheme {
        UploadWordsTopAppBar(
            state = UploadUiState.NoWords(
                categories = emptyList(),
                isExpandable = true,
                isSavable = true
            ),
            canNavigateBack = true,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}
