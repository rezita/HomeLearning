package com.github.rezita.homelearning.ui.screens.uploadwords

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
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun UploadWordItemDisplay(word: SpellingWord, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = word.word,
            Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_small)),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = getCategoryWithComment(word),
            Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_small)),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

private fun getCategoryWithComment(word: SpellingWord): String {
    val comment = if (word.comment != "") word.comment else "-"
    return word.category + " / " + comment
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun UploadWordItemPreview() {
    val spellingWord = SpellingWord(
        word = "appear",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    HomeLearningTheme {
        UploadWordItemDisplay(
            word = spellingWord
        )
    }
}
