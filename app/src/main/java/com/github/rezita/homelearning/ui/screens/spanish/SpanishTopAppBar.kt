package com.github.rezita.homelearning.ui.screens.spanish

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.screens.sentence.BooleanPreviewProvider
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpanishTopAppBar(
    @StringRes titleId: Int,
    state: SpanishUiState,
    action: SheetAction,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onUserEvent: (SpanishUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        title = stringResource(id = titleId),
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            when (action) {
                SheetAction.READ_ZITA_SPANISH_WORDS, SheetAction.READ_WEEK_SPANISH_WORDS -> {

                    if (state.isSavable()) {
                        IconButton(onClick = { onUserEvent(SpanishUserEvent.OnSave) }) {
                            Icon(
                                painterResource(id = R.drawable.ic_menu_check),
                                contentDescription = stringResource(id = R.string.sentences_check_and_save),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    if (state is SpanishUiState.Saved) {
                        IconButton(onClick = { onUserEvent(SpanishUserEvent.OnLoad) }) {
                            Icon(
                                painterResource(id = R.drawable.ic_menu_redo),
                                contentDescription = stringResource(id = R.string.menu_redo),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                else -> {}
            }
        },
        modifier = modifier
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpanishTopAppBar_success_can_navBack(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        SpanishTopAppBar(
            titleId = R.string.spanish_title,
            state = SpanishUiState.Loaded(words = emptyList()),
            action = SheetAction.READ_ZITA_SPANISH_WORDS,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            onUserEvent = {}
        )
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = 720,
    heightDp = 360
)
@Composable
private fun SpanishTopAppBar_success_readWords_showTranslate(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        val words = listOf(
            SpanishWord(
                wordEn = "Hello",
                wordSp = "Hola",
                comment = "",
                answer = "Hola",
                isWeekWord = false,
                enToSp = true,
            )
        )
        SpanishTopAppBar(
            titleId = R.string.spanish_title,
            state = SpanishUiState.Loaded(words = words),
            action = SheetAction.READ_SPANISH_WORDS,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            onUserEvent = {}
        )
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = 720,
    heightDp = 360
)
@Composable
private fun SpanishTopAppBar_success_readWords_NoShowTranslate(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        val words = listOf(
            SpanishWord(
                wordEn = "Hello",
                wordSp = "Hola",
                comment = "",
                answer = "Hola",
                isWeekWord = false,
                enToSp = true,
            )
        )
        SpanishTopAppBar(
            titleId = R.string.spanish_title,
            state = SpanishUiState.Loaded(words = words),
            action = SheetAction.READ_SPANISH_WORDS,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            onUserEvent = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpanishTopAppBar_success_notSavable(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        val words = listOf(
            SpanishWord(
                wordEn = "Hello",
                wordSp = "Hola",
                comment = "",
                answer = "Hola",
                isWeekWord = false,
                enToSp = true,
            )
        )
        SpanishTopAppBar(
            titleId = R.string.spanish_title,
            state = SpanishUiState.Loaded(words = words),
            action = SheetAction.READ_ZITA_SPANISH_WORDS,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            onUserEvent = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpanishTopAppBar_loading(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        SpanishTopAppBar(
            titleId = R.string.spanish_title,
            state = SpanishUiState.Loading,
            action = SheetAction.READ_ZITA_SPANISH_WORDS,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            onUserEvent = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpanishTopAppBar_error(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    HomeLearningTheme {
        SpanishTopAppBar(
            titleId = R.string.spanish_title,
            state = SpanishUiState.LoadingError(errorMessage = 12),
            action = SheetAction.READ_ZITA_SPANISH_WORDS,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            onUserEvent = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SpanishTopAppBar_saved(
    @PreviewParameter(BooleanPreviewProvider::class) canNavigateBack: Boolean,
) {
    val words = listOf(
        SpanishWord(
            wordEn = "Hello",
            wordSp = "Hola",
            comment = "",
            answer = "Hola",
            isWeekWord = false,
            enToSp = true,
        )
    )
    HomeLearningTheme {
        SpanishTopAppBar(
            titleId = R.string.spanish_title,
            state = SpanishUiState.Saved(words),
            action = SheetAction.READ_ZITA_SPANISH_WORDS,
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            onUserEvent = {}
        )
    }
}