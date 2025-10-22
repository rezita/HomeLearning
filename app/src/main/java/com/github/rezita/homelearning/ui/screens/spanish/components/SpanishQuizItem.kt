package com.github.rezita.homelearning.ui.screens.spanish.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.common.ResultIconWithText
import com.github.rezita.homelearning.ui.screens.common.SpeakerIconButton

@Composable
fun SpanishQuizItem(
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
private fun TextWithSpeaker(index: Int, word: SpanishWord, onSpeakerClicked: (String) -> Unit) {
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
            SpeakerIconButton { onSpeakerClicked(wordText) }
            Text("$wordText$commentText")
        }
    }
}

private fun wordWithIndexAndComment(index: Int, word: String, comment: String): String {
    return "${index + 1}. $word$comment"
}
