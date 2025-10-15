package com.github.rezita.homelearning.ui.screens.upload.spellingupload.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpellingUploadSavedContent(
    state: UploadUiState.Saved<SpellingWord>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.padding_extra_big),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            )
    ) {
        items(state.savingResult) { item ->
            SpellingSavedItemDisplay(wordResult = item)
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun SpellingSavedItemDisplay(
    wordResult: Pair<SpellingWord, String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        SpellingUploadItemDisplay(
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
private fun SpellingUploadSavedContent() {
    val spellingWord1 = SpellingWord(
        word = "appear",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    val spellingWord2 = SpellingWord(
        word = "disappear",
        category = "home",
        comment = "opposite",
        status = WordStatus.CORRECT
    )

    val state = UploadUiState.Saved(
        words = listOf(spellingWord1, spellingWord2),
        savingResult = listOf(Pair(spellingWord1, "Success"), Pair(spellingWord2, "Existed"))
    )
    HomeLearningTheme {
        Scaffold {
            SpellingUploadSavedContent(state = state, modifier = Modifier.padding(it))
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingSavedItemPreview() {
    val spellingWord = SpellingWord(
        word = "appear",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    HomeLearningTheme {
        SpellingSavedItemDisplay(wordResult = Pair(spellingWord, "Success"))
    }
}
