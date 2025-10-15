package com.github.rezita.homelearning.ui.screens.upload.spanishupload.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadSavedContent
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpanishSavedItem(
    wordResult: Pair<SpanishWord, String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        SpanishUploadWordItem(
            word = wordResult.first,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = wordResult.second,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpanishSavedItemPreview() {
    val word1 = SpanishWord(
        wordEn = "the apple",
        wordSp = "la manzana",
        comment = "with article",
        isWeekWord = false
    )
    HomeLearningTheme {
        SpanishSavedItem(wordResult = Pair(word1, "Success"))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpanishUploadSavedContent() {
    val word1 = SpanishWord(
        wordEn = "the apple",
        wordSp = "la manzana",
        comment = "with article",
        isWeekWord = false
    )

    val word2 = SpanishWord(
        wordEn = "I live",
        wordSp = "vivo",
        comment = "",
        isWeekWord = false
    )

    val word3 = SpanishWord(
        wordEn = "the apartment",
        wordSp = "el apartamento",
        comment = "",
        isWeekWord = false
    )

    val state = UploadUiState.Saved(
        words = listOf(word1, word2, word3),
        savingResult = listOf(
            Pair(word1, "Success"),
            Pair(word2, "Existed"),
            Pair(word3, "Success")
        )
    )
    HomeLearningTheme {
        Scaffold {
            UploadSavedContent(
                state = state,
                modifier = Modifier.padding(it)
            ) { wordResult -> SpanishSavedItem(wordResult) }
        }
    }
}