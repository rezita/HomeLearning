package com.github.rezita.homelearning.ui.screens.spelling

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.viewmodels.SpellingViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SpellingScreen(
    viewModel: SpellingViewModel,
    modifier: Modifier = Modifier
) {
    val spellingState by viewModel.spellingUIState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SpellingTopAppBar(
                state = spellingState,
                saveCallback = { viewModel.saveSpellingResults() }
            )
        }
    ) {
        when (val state = spellingState) {
            is NormalRepositoryResult.Downloading ->
                LoadingProgressBar()

            is NormalRepositoryResult.Downloaded -> {
                SpellingItems(
                    words = state.data,
                    onValueChange = { index, status -> viewModel.updateWordStatus(index, status) },
                    modifier = Modifier.padding(it)
                )
            }

            is NormalRepositoryResult.DownloadingError -> {
                LoadingErrorSnackbar(scope = scope, snackbarHostState = snackbarHostState)
                ErrorDisplayInColumn(
                    message = state.message,
                    callback = { viewModel.load() })

            }

            is NormalRepositoryResult.Uploading ->
                LoadingProgressBar()

            is NormalRepositoryResult.Uploaded -> {
                SavingSuccessSnackbar(scope = scope, snackbarHostState = snackbarHostState)
                SpellingItems(
                    words = state.data,
                    onValueChange = { index, status -> viewModel.updateWordStatus(index, status) },
                    isEnabled = false,
                    modifier = Modifier.padding(it)
                )
            }

            is NormalRepositoryResult.UploadError -> {
                SavingErrorSnackbar(scope = scope, snackbarHostState = snackbarHostState)
                ErrorDisplayWithContent(
                    message = state.message,
                    callback = { viewModel.saveSpellingResults() },
                    content = {
                        SpellingItems(
                            words = state.data,
                            onValueChange = { index, status -> {} },
                            isEnabled = false,
                        )
                    },
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}

@Composable
private fun SpellingItem(
    index: Int,
    word: String,
    wordStatus: WordStatus,
    isEnabled: Boolean = true,
    onItemSelected: (WordStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = getIndexPrefix(index),
            Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_small))
                .width(28.dp)
        )
        Text(
            text = word,
            Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_small))
                .weight(1f),
        )
        SpellingRadioGroup(
            selected = wordStatus,
            setSelected = onItemSelected,
            isEnabled = isEnabled,
        )

    }
}

private fun getIndexPrefix(index: Int): String {
    return "${index + 1}".padStart(1, ' ') + "."
}


@Composable
private fun SpellingItems(
    words: List<SpellingWord>,
    onValueChange: (Int, WordStatus) -> Unit,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = getScores(words),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            textAlign = TextAlign.Right,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(words) { index, item ->
                SpellingItem(
                    index = index,
                    word = item.word,
                    wordStatus = item.status,
                    onItemSelected = { status -> onValueChange(index, status) },
                    isEnabled = isEnabled
                )
                Divider(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun getScores(words: List<SpellingWord>): String {
    val nrOfQuestions = words.filter { it.status != WordStatus.UNCHECKED }.size
    val nrOfCorrect = words.filter { it.status == WordStatus.CORRECT }.size
    val ratio = if (nrOfQuestions == 0) 0 else nrOfCorrect * 100 / nrOfQuestions
    return stringResource(
        R.string.spelling_result,
        nrOfCorrect,
        nrOfQuestions,
        ratio
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingItemPreview() {
    HomeLearningTheme {
        SpellingItem(
            index = 17,
            word = "kalacs",
            wordStatus = WordStatus.CORRECT,
            onItemSelected = {})
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingItemDisabledPreview() {
    HomeLearningTheme {
        SpellingItem(
            index = 0,
            word = "kalacs",
            wordStatus = WordStatus.CORRECT,
            isEnabled = false,
            onItemSelected = {})
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingItemsPreview() {
    val spelling1 = SpellingWord(
        word = "appear",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    val spelling2 = SpellingWord(
        word = "behave",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    val spelling3 = SpellingWord(
        word = "boutique",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.INCORRECT
    )
    val spelling4 = SpellingWord(
        word = "destructive",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.UNCHECKED
    )
    val words = listOf(spelling1, spelling2, spelling3, spelling4)
    HomeLearningTheme {
        Scaffold() {
            SpellingItems(
                words = words,
                onValueChange = { index, status -> {} },
                modifier = Modifier.padding(it)
            )
        }
    }
}

