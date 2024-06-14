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
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.screens.sentence.BooleanPreviewProvider
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpellingTopAppBar(
    state: SpellingUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    saveCallback: () -> Unit,
    addNewCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        title = stringResource(id = R.string.activity_spelling),

        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            if (state.isSavable()) {
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
private fun SpellingTopAppBar_loading(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean
) {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = SpellingUiState.Loading,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpellingTopAppBar_loaded_empty(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean
) {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = SpellingUiState.Loaded(words = emptyList()),
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpellingTopAppBar_loaded_savable(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean
) {
    HomeLearningTheme {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(spelling1, spelling2)
        SpellingTopAppBar(
            state = SpellingUiState.Loaded(words = words),
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpellingTopAppBar_upload_success(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean
) {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = SpellingUiState.Saved(words = emptyList()),
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            addNewCallback = {}
        )
    }
}