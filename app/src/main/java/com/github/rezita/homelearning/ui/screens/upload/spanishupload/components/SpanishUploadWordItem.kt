package com.github.rezita.homelearning.ui.screens.upload.spanishupload.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpanishUploadWordItem(word: SpanishWord, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = getEnWithSpWord(word),
            Modifier
                .padding(
                    end = dimensionResource(id = R.dimen.padding_small),
                    top = dimensionResource(id = R.dimen.padding_small)
                ),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = getCommentText(word),
            Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_small)),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun getEnWithSpWord(word: SpanishWord): String = "${word.wordEn} / ${word.wordSp}"

private fun getCommentText(word: SpanishWord): String {
    return if (word.comment != "") word.comment else "-"
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpanishUploadItemPreview() {
    val word = SpanishWord(
        wordEn = "the apple",
        wordSp = "la manzana",
        comment = "with article",
        isWeekWord = false
    )
    HomeLearningTheme {
        SpanishUploadWordItem(word)
    }
}
