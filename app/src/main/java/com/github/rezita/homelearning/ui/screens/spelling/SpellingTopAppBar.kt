package com.github.rezita.homelearning.ui.screens.spelling

import android.content.res.Configuration
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
fun SpellingTopAppBar(
    state: SpellingUiState,
    canNavigateBack: Boolean,
    navigateUp: ()->Unit,
    saveCallback: () -> Unit,
    addNewCallback: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        titleText = when (state) {
            is SpellingUiState.Loaded -> stringResource(id = R.string.activity_spelling)
            is SpellingUiState.Loading -> stringResource(id = R.string.app_bar_loading_title)
            is SpellingUiState.LoadingError -> stringResource(id = R.string.app_bar_error_title)
            is SpellingUiState.Saved -> stringResource(id = R.string.activity_spelling)
            is SpellingUiState.SavingError -> stringResource(id = R.string.app_bar_error_title)
        },

        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            if (state.isSavable) {
                IconButton(onClick = saveCallback) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_result),
                        contentDescription = stringResource(id = R.string.spelling_save),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            if (state is SpellingUiState.Loaded || state is SpellingUiState.Saved) {
                IconButton(onClick = { addNewCallback() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_add),
                        contentDescription = stringResource(id = R.string.spelling_add_new_word),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SpellingTopAppBar_loading() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = SpellingUiState.Loading(isSavable = false),
            canNavigateBack = true,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SpellingTopAppBar_downloaded_success() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = SpellingUiState.Loaded(words = emptyList(), isSavable = true),
            canNavigateBack = false,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SpellingTopAppBar_download_error() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = SpellingUiState.LoadingError(errorMessage = 12, isSavable = false),
            canNavigateBack = false,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SpellingTopAppBar_upload_success() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = SpellingUiState.Saved(words = emptyList(), isSavable = false),
            canNavigateBack = false,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}