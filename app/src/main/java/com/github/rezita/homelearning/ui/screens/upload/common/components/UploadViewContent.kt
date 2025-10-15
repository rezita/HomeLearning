package com.github.rezita.homelearning.ui.screens.upload.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.Uploadable
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun <T : Uploadable> UploadViewContent(
    state: UploadUiState.HasWords<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (Int, T) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.padding_extra_big),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            )
    ) {
        itemsIndexed(state.words) { index, item ->
            itemContent(index, item)
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

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun UploadViewContentPreview() {
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

    val state = UploadUiState.HasWords(
        words = listOf(spellingWord1, spellingWord2)
    )
    HomeLearningTheme {
        Scaffold {
            UploadViewContent(
                state = state,
                modifier = Modifier.padding(it),
                { index, item -> Text("$index. ${item.word}") }
            )
        }
    }
}

