package com.github.rezita.homelearning.ui.screens.sentence

import android.content.res.Configuration
import androidx.annotation.StringRes
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
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SentenceTopAppBar(
    @StringRes titleId: Int,
    state: SentenceUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    saveCallback: () -> Unit,
    redoCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        title = stringResource(id = titleId),
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            if (state.isSavable()) {
                IconButton(onClick = { saveCallback() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_check),
                        contentDescription = stringResource(id = R.string.sentences_check_and_save),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            if (state is SentenceUiState.Saved) {
                IconButton(onClick = { redoCallback() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_redo),
                        contentDescription = stringResource(id = R.string.menu_redo),
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
private fun SentenceTopAppBar_success_can_navBack(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        SentenceTopAppBar(
            titleId = R.string.activity_irregular_verbs,
            state = SentenceUiState.Loaded(sentences = emptyList()),
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            redoCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SentenceTopAppBar_success_notSavable(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        val sentences = listOf(
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )
        )
        SentenceTopAppBar(
            titleId = R.string.activity_irregular_verbs,
            state = SentenceUiState.Loaded(sentences = sentences),
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            redoCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SentenceTopAppBar_loading(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        SentenceTopAppBar(
            titleId = R.string.activity_homophones,
            state = SentenceUiState.Loading,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            redoCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SentenceTopAppBar_error(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        SentenceTopAppBar(
            titleId = R.string.activity_homophones,
            state = SentenceUiState.LoadingError(errorMessage = 12),
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            redoCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SentenceTopAppBar_saved(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    val sentences = listOf(
        FillInSentence(
            sentence = "I have never been to Italy.",
            suggestion = "be",
            solutions = listOf("been"),
            answer = "was"
        )
    )

    HomeLearningTheme {
        SentenceTopAppBar(
            titleId = R.string.activity_homophones,
            state = SentenceUiState.Saved(sentences = sentences),
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            saveCallback = {},
            redoCallback = {}
        )
    }
}