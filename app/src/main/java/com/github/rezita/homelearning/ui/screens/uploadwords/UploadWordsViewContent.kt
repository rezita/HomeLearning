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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun UploadWordsViewContent(
    state: UploadUiState.HasWords,
    onWordEdit: (Int) -> Unit,
    onWordDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.padding_big),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            )
    ) {
        itemsIndexed(state.words) { index, item ->
            UploadItemDisplay(
                word = item,
                onDeleteCallback = { onWordDelete(index) },
                onEditCallback = { onWordEdit(index) },
            )
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
private fun UploadItemDisplay(
    word: SpellingWord,
    onDeleteCallback: () -> Unit,
    onEditCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val openConfirmDialog = remember { mutableStateOf(false) }

    when {
        openConfirmDialog.value ->
            DeleteConfirmDialog(
                onDismissRequest = {
                    openConfirmDialog.value = false
                },
                onConfirmation = {
                    onDeleteCallback()
                    openConfirmDialog.value = false
                },
            )

    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(modifier = Modifier.weight(1f)) {
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
                style = MaterialTheme.typography.labelSmall,
            )
        }
        WordManipulationIcons(
            onDeleteCallback = { openConfirmDialog.value = true },
            onEditCallback = { onEditCallback() })
    }
}

private fun getCategoryWithComment(word: SpellingWord): String {
    val comment = if (word.comment != "") word.comment else "-"
    return word.category + " / " + comment
}

@Composable
private fun WordManipulationIcons(
    onDeleteCallback: () -> Unit,
    onEditCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onDeleteCallback) {
            Icon(
                painterResource(id = R.drawable.ic_delete),
                contentDescription = stringResource(id = R.string.upload_delete_word),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        IconButton(onClick = onEditCallback) {
            Icon(
                painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.upload_edit_word),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun UploadItemPreview() {
    val spellingWord = SpellingWord(
        word = "appear",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    HomeLearningTheme {
        UploadItemDisplay(
            word = spellingWord,
            onDeleteCallback = {},
            onEditCallback = {}
        )
    }
}