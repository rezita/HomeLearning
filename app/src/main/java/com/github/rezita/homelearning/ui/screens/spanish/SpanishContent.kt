package com.github.rezita.homelearning.ui.screens.spanish

import android.content.res.Configuration
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithIcon
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.screens.common.reading.ReadingContent
import com.github.rezita.homelearning.ui.screens.spanish.components.SpanishQuizContent
import com.github.rezita.homelearning.ui.screens.spanish.components.SpanishReadingItem
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpanishContent(
    state: SpanishUiState,
    action: SheetAction,
    windowSize: HomeLearningWindowSizeClass,
    orientation: Int,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onUserEvent: (SpanishUserEvent) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is SpanishUiState.Loading -> {
            LoadingProgressBar(modifier = modifier)
        }

        is SpanishUiState.Loaded -> {
            when (action) {
                SheetAction.READ_ZITA_SPANISH_WORDS, SheetAction.READ_WEEK_SPANISH_WORDS -> {
                    SpanishQuizContent(
                        words = state.words,
                        isAllAnswered = state.isSavable(),
                        onUserEvent = onUserEvent,
                        modifier = modifier.imePadding(),
                    )
                }

                SheetAction.READ_SPANISH_WORDS -> {
                    when (orientation) {
                        Configuration.ORIENTATION_PORTRAIT ->
                            ErrorDisplayWithIcon(
                                message = stringResource(id = R.string.msg_turn_landscape_mode),
                                iconSource = R.drawable.screen_rotation_24px,
                                modifier = modifier.imePadding()
                            )

                        else -> {
                            ReadingContent(
                                nrOfPages = state.words.size,
                                modifier = modifier.imePadding(),
                            ) { page ->
                                SpanishReadingItem(
                                    windowSize = windowSize,
                                    word = state.words[page],
                                    onSpeakerClicked = {
                                        onUserEvent(SpanishUserEvent.OnSpeakerClicked(it))
                                    },
                                    navigateUp = navigateUp
                                )
                            }
                        }

                    }
                }

                else -> {
                    LoadingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
                    ErrorDisplayInColumn(
                        message = stringResource(R.string.snackBar_error_loading),
                        callback = { onUserEvent(SpanishUserEvent.OnLoad) },
                        modifier = modifier
                    )
                }
            }
        }

        is SpanishUiState.LoadingError -> {
            LoadingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayInColumn(
                message = stringResource(id = state.errorMessage),
                callback = { onUserEvent(SpanishUserEvent.OnLoad) },
                modifier = modifier
            )
        }

        is SpanishUiState.Saved -> {
            SavingSuccessSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            SpanishQuizContent(
                words = state.words,
                isAllAnswered = true,
                onUserEvent = onUserEvent,
                modifier = modifier,
                showResults = true
            )
        }

        is SpanishUiState.SavingError -> {
            SavingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayWithContent(
                message = stringResource(id = state.errorMessage),
                callback = { onUserEvent(SpanishUserEvent.OnSave) },
                content = {
                    SpanishQuizContent(
                        words = state.words,
                        isAllAnswered = true,
                        onUserEvent = onUserEvent,
                        showResults = true
                    )
                },
                modifier = modifier,
            )
        }
    }
}

@Preview(
    name = "small min 1f",
    showBackground = true,
    device = "spec:width=600dp,height=320dp,dpi=160",
    showSystemUi = false
)
@Composable
private fun SpanishContent_reading(
) {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    val windowSize =
        HomeLearningWindowSizeClass.calculateFromSize(size)
    val word = SpanishWord(
        wordEn = "the computer",
        wordSp = "la computadora",
        comment = "with comment",
        answer = "el compoter",
        isWeekWord = false,
        enToSp = false,
    )
    val words = listOf(word)

    HomeLearningTheme {
        Scaffold {
            SpanishContent(
                state = SpanishUiState.Loaded(words),
                action = SheetAction.READ_SPANISH_WORDS,
                windowSize = windowSize,
                orientation = configuration.orientation,
                scope = rememberCoroutineScope(),
                snackBarHostState = SnackbarHostState(),
                onUserEvent = {},
                navigateUp = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}

