package com.github.rezita.homelearning.ui.screens.spanish.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.ui.screens.common.SpeakerIconButton
import com.github.rezita.homelearning.utils.toDp

@Composable
internal fun TextWithSpeaker(index: Int, word: SpanishWord, onSpeakerClicked: (String) -> Unit) {
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

@Composable
internal fun SimpleTextWithSpeaker(
    text: String,
    showSpeaker: Boolean,
    style: TextStyle = LocalTextStyle.current,
    onSpeakerClicked: (String) -> Unit,
) {
    val fontSizeToDp = style.fontSize.toDp()
    val speakerSize = if (fontSizeToDp < 48.dp) 48.dp else fontSizeToDp
    val paddingEnd = if (showSpeaker) speakerSize else 0.dp
    Row(
        modifier = Modifier.padding(end = paddingEnd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showSpeaker) {
            SpeakerIconButton(modifier = Modifier.size(speakerSize)) {
                onSpeakerClicked(
                    text
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
        }
        Text(text, maxLines = 1, style = style)
    }
}
