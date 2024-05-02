package com.github.rezita.homelearning.ui.screens.uploadwords

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun UploadWordsSavedContent(
    state: UploadUiState.Saved,
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
            SavedItemDisplay(wordResult = item)
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
private fun SavedItemDisplay(
    wordResult: Pair<String, String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = wordResult.first,
                Modifier
                    .padding(end = dimensionResource(id = R.dimen.padding_small)),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = wordResult.second,
                Modifier
                    .padding(end = dimensionResource(id = R.dimen.padding_small)),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun UploadItemPreview() {
    HomeLearningTheme {
        SavedItemDisplay(wordResult = Pair("appear", "Success"))
    }
}
