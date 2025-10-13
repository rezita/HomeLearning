package com.github.rezita.homelearning.ui.screens.spellingupload.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.spellingupload.SpellingUploadUiState
import com.github.rezita.homelearning.ui.screens.spellingupload.SpellingUploadUserEvent
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun UploadWordsViewContent(
    state: SpellingUploadUiState.HasWords,
    onUserEvent: (SpellingUploadUserEvent) -> Unit,
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
        itemsIndexed(state.words) { index, item ->
            UploadItemDisplay(
                word = item,
                onDeleteCallback = { onUserEvent(SpellingUploadUserEvent.OnRemoveSpelling(index)) },
                onEditCallback = { onUserEvent(SpellingUploadUserEvent.OnPrepareForEditing(index)) },
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
            .padding(start = dimensionResource(id = R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        UploadWordItemDisplay(word = word, modifier = Modifier.weight(1f))
        WordManipulationIcons(
            onDeleteCallback = { openConfirmDialog.value = true },
            onEditCallback = { onEditCallback() })
    }
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
        IconButton(onClick = onEditCallback) {
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = stringResource(id = R.string.upload_edit_word),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        IconButton(onClick = onDeleteCallback) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(id = R.string.upload_delete_word),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun UploadWordsViewContentPreview() {
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

    val state = SpellingUploadUiState.HasWords(
        words = listOf(spellingWord1, spellingWord2)
    )
    HomeLearningTheme {
        Scaffold {
            UploadWordsViewContent(
                state = state,
                onUserEvent = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun UploadItemDisplayPreview() {
    val spellingWord = SpellingWord(
        word = "appear",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    HomeLearningTheme {
        Scaffold {
            UploadItemDisplay(
                word = spellingWord,
                onDeleteCallback = {},
                onEditCallback = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}