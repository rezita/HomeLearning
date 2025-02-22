package com.github.rezita.homelearning.ui.screens.sentence

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.theme.sentence_correct
import com.github.rezita.homelearning.ui.theme.sentence_incorrect
import com.github.rezita.homelearning.utils.getWithResult
import com.github.rezita.homelearning.utils.splitBySeparatorWithSuggestion
import kotlinx.coroutines.CoroutineScope

@Composable
fun SentenceContent(
    state: SentenceUiState,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onUserEvent: (SentenceUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is SentenceUiState.Loading -> {
            LoadingProgressBar(modifier = modifier)
        }

        is SentenceUiState.Loaded -> SentenceItems(
            sentences = state.sentences,
            isAllAnswered = state.isSavable(),
            onUserEvent = onUserEvent,
            modifier = modifier.imePadding()
        )

        is SentenceUiState.LoadingError -> {
            LoadingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayInColumn(
                message = stringResource(id = state.errorMessage),
                callback = { onUserEvent(SentenceUserEvent.OnLoad) },
                modifier = modifier
            )
        }

        is SentenceUiState.Saved -> {
            SavingSuccessSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            SentenceResultScreen(sentences = state.sentences, modifier = modifier)
        }


        is SentenceUiState.SavingError -> {
            SavingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayWithContent(
                message = stringResource(id = state.errorMessage),
                callback = { onUserEvent(SentenceUserEvent.OnSave) },
                content = { SentenceResultScreen(sentences = state.sentences) },
                modifier = modifier
            )
        }
    }
}


@Composable
fun SentenceItems(
    sentences: List<FillInSentence>,
    isAllAnswered: Boolean,
    onUserEvent: (SentenceUserEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val openConfirmDialog = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val kc = LocalSoftwareKeyboardController.current

    when {
        openConfirmDialog.value -> SaveConfirmDialog(
            onDismissRequest = {
                openConfirmDialog.value = false
            },
            onConfirmation = {
                onUserEvent(SentenceUserEvent.OnSave)
                openConfirmDialog.value = false
            },
        )
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsIndexed(sentences) { index, item ->
            val (prefix, suffix) = item.splitBySeparatorWithSuggestion()
            val prefixWithIndex = "${getIndexPrefix(index)} $prefix"
            PartiallyEditableText(prefix = prefixWithIndex,
                suffix = suffix,
                value = item.answer,
                onValueChange = { value ->
                    onUserEvent(
                        SentenceUserEvent.OnValueChange(
                            index, value
                        )
                    )
                },
                baseTextColor = MaterialTheme.colorScheme.onSurface,
                label = null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = if (isAllAnswered) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    // Pressing Ime button would move the text indicator's focus to the next field (or the first textField)
                    focusManager.moveFocus(FocusDirection.Next)
                },
                    // Pressing Ime button would call the onDoneCallback
                    onDone = {
                        kc?.hide()
                        openConfirmDialog.value = true
                    }),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedPrefixColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedPrefixColor = MaterialTheme.colorScheme.onSurface,
                    errorPrefixColor = MaterialTheme.colorScheme.onSurface,
                    disabledPrefixColor = MaterialTheme.colorScheme.onSurface,
                    focusedSuffixColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedSuffixColor = MaterialTheme.colorScheme.onSurface,
                    errorSuffixColor = MaterialTheme.colorScheme.onSurface,
                    disabledSuffixColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier.fillMaxWidth())
        }
    }
}


@Composable
fun SentenceResultScreen(
    sentences: List<FillInSentence>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = getScores(sentences),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            textAlign = TextAlign.Right,
        )
        SentenceResultItems(
            sentences = sentences,
        )
    }
}

@Composable
private fun getScores(sentences: List<FillInSentence>): String {
    val nrOfQuestions = sentences.size
    val nrOfCorrect = sentences.filter { it.status == WordStatus.CORRECT }.size
    val ratio = if (nrOfQuestions == 0) 0 else nrOfCorrect * 100 / nrOfQuestions
    return stringResource(
        R.string.irregular_verb_result, nrOfCorrect, nrOfQuestions, ratio
    )
}

@Composable
fun SentenceResultItems(
    sentences: List<FillInSentence>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsIndexed(sentences) { index, item ->
            Row(modifier = modifier.fillMaxSize()) {
                if (item.status == WordStatus.CORRECT) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.sentences_result_correct),
                        tint = sentence_correct,
                        modifier = Modifier.padding(
                            start = dimensionResource(id = R.dimen.padding_small),
                            top = dimensionResource(id = R.dimen.padding_medium)
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.sentences_result_incorrect),
                        tint = sentence_incorrect,
                        modifier = Modifier.padding(
                            start = dimensionResource(id = R.dimen.padding_small),
                            top = dimensionResource(id = R.dimen.padding_medium)
                        )
                    )
                }
                Text(
                    text = getResultWithIndex(
                        index, item.getWithResult(
                            sentence_correct,
                            sentence_incorrect,
                        )
                    ), textAlign = TextAlign.Left, modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_medium),
                        top = dimensionResource(id = R.dimen.padding_medium)
                    )
                )
            }
        }
    }
}

private fun getResultWithIndex(index: Int, resultText: AnnotatedString): AnnotatedString {
    val annotated = buildAnnotatedString {
        append(getIndexPrefix(index))
        append(" ")
        append(resultText)
    }
    return annotated
}

private fun getIndexPrefix(index: Int): String {
    return "${index + 1}."
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SentenceItemsPreview() {
    val sentence1 = FillInSentence(
        sentence = "I accidentally \$£ on the dog's foot and it yelped.",
        suggestion = "tread",
        solutions = listOf("trod", "treaded"),
        answer = "trod",
        tense = "present",
    )
    val sentence2 = FillInSentence(
        sentence = "I have never $£ to Italy.", suggestion = "be", solutions = listOf("been")
    )
    val sentences = listOf(sentence1, sentence2)
    HomeLearningTheme {
        SentenceItems(sentences = sentences, isAllAnswered = false, onUserEvent = {})
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SentenceResultPreview() {
    val sentence1 = FillInSentence(
        sentence = "I accidentally \$£ on the dog's foot and it yelped.",
        suggestion = "tread",
        solutions = listOf("trod", "treaded"),
        answer = "trod",
        tense = "present",
    )
    val sentence2 = FillInSentence(
        sentence = "I have never $£ to Italy.",
        suggestion = "be",
        solutions = listOf("been"),
        answer = "did",
    )
    val sentences = listOf(sentence1, sentence2)
    HomeLearningTheme {
        Scaffold {
            SentenceResultScreen(sentences = sentences, modifier = Modifier.padding(it))
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SentenceUploadErrorPreview() {
    val sentence1 = FillInSentence(
        sentence = "I accidentally \$£ on the dog's foot and it yelped.",
        suggestion = "tread",
        solutions = listOf("trod", "treaded"),
        answer = "trod",
        tense = "present",
    )
    val sentence2 = FillInSentence(
        sentence = "I have never $£ to Italy.",
        suggestion = "be",
        solutions = listOf("been"),
        answer = "did",
    )
    val sentences = listOf(sentence1, sentence2)
    HomeLearningTheme {
        Scaffold {
            ErrorDisplayWithContent(message = "This will be the error message",
                callback = {},
                content = { SentenceResultScreen(sentences = sentences) },
                modifier = Modifier.padding(it)
            )
        }
    }
}