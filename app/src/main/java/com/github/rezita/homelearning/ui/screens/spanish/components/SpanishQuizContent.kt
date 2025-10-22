package com.github.rezita.homelearning.ui.screens.spanish.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.sentence.SaveConfirmDialog
import com.github.rezita.homelearning.ui.screens.spanish.SpanishUserEvent
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpanishQuizContent(
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
    Column(modifier = modifier.fillMaxWidth()) {
        if (showResults) {
            SpanishResultContent(words)
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                SpanishQuizItem(
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
private fun SpanishResultContent(
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
        Scaffold {
            SpanishQuizContent(
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
        Scaffold {
            SpanishQuizContent(
                words = words,
                isAllAnswered = false,
                onUserEvent = {},
                showResults = true,
                modifier = Modifier.padding(it)
            )
        }
    }
}
