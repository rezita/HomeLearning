package com.github.rezita.homelearning.ui.screens.sentence

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
fun SentenceTopAppBar(
    state: SentenceUiState,
    callback: () -> Unit,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        titleText = when (state) {
            is SentenceUiState.Loaded -> stringResource(id = R.string.activity_sentences)
            is SentenceUiState.Loading -> stringResource(id = R.string.app_bar_loading_title)
            is SentenceUiState.LoadingError -> stringResource(id = R.string.app_bar_error_title)
            is SentenceUiState.Saved -> stringResource(id = R.string.activity_sentences)
            is SentenceUiState.SavingError -> stringResource(id = R.string.app_bar_error_title)
        },

        navigateUp = {},

        actions = {
            if (state.isSavable) {
                IconButton(onClick = { callback() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_check),
                        contentDescription = stringResource(id = R.string.sentences_check_and_save),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
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
fun SentenceTopAppBar_success_notAllAnswered() {
    HomeLearningTheme {
        SentenceTopAppBar(
            state = SentenceUiState.Loaded(sentences = emptyList(), isSavable = false),
            callback = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_success_allAnswered() {
    HomeLearningTheme {
        SentenceTopAppBar(
            state = SentenceUiState.Loaded(sentences = emptyList(), isSavable = true),
            callback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_loading() {
    HomeLearningTheme {
        SentenceTopAppBar(
            state = SentenceUiState.Loading(false),
            callback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_error() {
    HomeLearningTheme {
        SentenceTopAppBar(
            state = SentenceUiState.LoadingError(isSavable = false, errorMessage = 12),
            callback = {},
        )
    }
}