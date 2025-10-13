package com.github.rezita.homelearning.ui.screens.spanish

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.ResultIconWithText
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.screens.common.SpeakerIconButton
import com.github.rezita.homelearning.ui.screens.sentence.SaveConfirmDialog
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpanishContent(
    state: SpanishUiState,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onUserEvent: (SpanishUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is SpanishUiState.Loading -> {
            LoadingProgressBar(modifier = modifier)
        }

        is SpanishUiState.Loaded -> {
            SpanishItems(
                words = state.words,
                isAllAnswered = state.isSavable(),
                onUserEvent = onUserEvent,
                modifier = modifier.imePadding(),
            )
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
            SpanishItems(
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
                    SpanishItems(
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

@Composable
fun SpanishItems(
    words: List<SpanishWord>,
    isAllAnswered: Boolean,
    onUserEvent: (SpanishUserEvent) -> Unit,
    modifier: Modifier = Modifier,
    showResults: Boolean = false
) {
    val openConfirmDialog = remember { mutableStateOf(false) }
    val imeAction = if (isAllAnswered) ImeAction.Done else ImeAction.Next

    when {
        openConfirmDialog.value -> SaveConfirmDialog(
            onDismissRequest = {
                openConfirmDialog.value = false
            },
            onConfirmation = {
                onUserEvent(SpanishUserEvent.OnSave)
                openConfirmDialog.value = false
            },
        )
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        if (showResults) {
            SpanishResultContent(words)
        }
        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(words) { index, item ->
                val focusManager = LocalFocusManager.current
                val kc = LocalSoftwareKeyboardController.current

                val keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    // Pressing Ime button would call the onDoneCallback
                    onDone = {
                        kc?.hide()
                        openConfirmDialog.value = true
                    })

                HorizontalDivider()
                SpanishItem(
                    index, item, showResults, imeAction, keyboardActions,
                    onSpeakerClicked = {
                        onUserEvent(SpanishUserEvent.OnSpeakerClicked(it))
                    },
                    onValueChange = {
                        onUserEvent(
                            SpanishUserEvent.OnValueChange(index, it)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SpanishResultContent(
    words: List<SpanishWord>,
    modifier: Modifier = Modifier,
) {
    Text(
        text = getScores(words),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        textAlign = TextAlign.Right,
    )
}


@Composable
private fun getScores(words: List<SpanishWord>): String {
    val nrOfQuestions = words.size
    val nrOfCorrect = words.filter { it.status == WordStatus.CORRECT }.size
    val ratio = if (nrOfQuestions == 0) 0 else nrOfCorrect * 100 / nrOfQuestions
    return stringResource(
        R.string.irregular_verb_result, nrOfCorrect, nrOfQuestions, ratio
    )
}


@Composable
fun SpanishItem(
    index: Int,
    word: SpanishWord,
    showResults: Boolean,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
    onSpeakerClicked: (String) -> Unit,
    onValueChange: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        TextWithSpeaker(index, word, onSpeakerClicked)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(R.dimen.padding_medium))
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.padding_small))
            )
            Text(text = stringResource(R.string.answer))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.padding_small))
            )
            OutlinedTextField(
                value = word.answer,
                onValueChange = { onValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !showResults,
                singleLine = false,
                minLines = 1,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                )
            )
            if (showResults) {
                ResultIconWithText(word.status)
                if (word.status != WordStatus.CORRECT) {
                    Text(stringResource(R.string.correct_aswer, word.solution))
                }
            }
        }
    }
}

@Composable
fun TextWithSpeaker(index: Int, word: SpanishWord, onSpeakerClicked: (String) -> Unit) {
    val wordText = if (word.enToSp) word.wordEn else word.wordSp
    val commentText = if (word.comment.isNotEmpty()) " (${word.comment})" else ""
    if (word.enToSp) {
        Text(text = wordWithIndexAndComment(index, wordText, commentText))
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${index + 1}.")
            SpeakerIconButton({ onSpeakerClicked(wordText) })
            Text("$wordText$commentText")
        }
    }
}

fun wordWithIndexAndComment(index: Int, word: String, comment: String): String {
    return "${index + 1}. $word$comment"
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SpanishItemsPreview_not_show_result() {
    val words = listOf(
        SpanishWord(
            wordEn = "Hello",
            wordSp = "Hola",
            comment = "",
            answer = "Hola",
            isWeekWord = false,
            enToSp = true,
        ),
        SpanishWord(
            wordEn = "the tomato",
            wordSp = "el tomate",
            comment = "with article",
            answer = "el tomare",
            isWeekWord = false,
            enToSp = false,
        )
    )
    HomeLearningTheme {
        Scaffold() {
            SpanishItems(
                words = words,
                isAllAnswered = false,
                onUserEvent = {},
                showResults = false,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SpanishItemsPreview_show_result() {
    val words = listOf(
        SpanishWord(
            wordEn = "Hello",
            wordSp = "Hola",
            comment = "",
            answer = "Hola",
            isWeekWord = false,
            enToSp = true,
        ),
        SpanishWord(
            wordEn = "the tomato",
            wordSp = "el tomate",
            comment = "with comment",
            answer = "el tomare",
            isWeekWord = false,
            enToSp = false,
        )
    )
    HomeLearningTheme {
        Scaffold() {
            SpanishItems(
                words = words,
                isAllAnswered = false,
                onUserEvent = {},
                showResults = true,
                modifier = Modifier.padding(it)
            )
        }
    }
}
