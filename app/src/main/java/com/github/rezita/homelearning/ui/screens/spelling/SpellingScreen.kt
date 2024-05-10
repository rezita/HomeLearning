package com.github.rezita.homelearning.ui.screens.spelling

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.viewmodels.SpellingViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpellingScreen(
    sheetAction: SheetAction,
    viewModel: SpellingViewModel = viewModel(
        factory = SpellingViewModel.SpellingViewModelFactory(
            sheetAction
        )
    ),
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    addNewCallback: () -> Unit = {},
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val spellingUiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val rbContentType = getRadioButtonType(windowSize)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            SpellingTopAppBar(
                state = spellingUiState,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                saveCallback = { viewModel.saveSpellingResults() },
                addNewCallback = addNewCallback
            )
        }
    ) {
        SpellingContent(
            state = spellingUiState,
            viewModel = viewModel,
            scope = scope,
            snackBarHostState = snackBarHostState,
            rbContentType = rbContentType,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun SpellingContent(
    state: SpellingUiState,
    viewModel: SpellingViewModel,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    rbContentType: RadioButtonContentType,
    modifier: Modifier = Modifier
) {
    when (state) {
        is SpellingUiState.Loading -> {
            LoadingProgressBar(modifier = modifier)
        }

        is SpellingUiState.Loaded -> {
            SpellingItems(
                words = state.words,
                onValueChange = { index, status -> viewModel.updateWordStatus(index, status) },
                onItemReset = { index -> viewModel.resetWordStatus(index) },
                rbContentType = rbContentType,
                modifier = modifier
            )
        }

        is SpellingUiState.LoadingError -> {
            LoadingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayInColumn(
                message = stringResource(id = state.errorMessage),
                callback = { viewModel.load() },
                modifier = modifier
            )
        }

        is SpellingUiState.Saved -> {
            SavingSuccessSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            SpellingItems(
                words = state.words,
                onValueChange = { _, _ -> run {} },
                rbContentType = rbContentType,
                isEnabled = false,
                modifier = modifier
            )
        }

        is SpellingUiState.SavingError -> {
            SavingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayWithContent(
                message = stringResource(id = state.errorMessage),
                callback = { viewModel.saveSpellingResults() },
                content = {
                    SpellingItems(
                        words = state.words,
                        onValueChange = { _, _ -> run {} },
                        rbContentType = rbContentType,
                        isEnabled = false,
                    )
                },
                modifier = modifier
            )
        }
    }
}


@Composable
private fun SpellingItem(
    index: Int,
    word: String,
    wordStatus: WordStatus,
    onItemSelected: (WordStatus) -> Unit,
    onItemReset: () -> Unit,
    rbContentType: RadioButtonContentType,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val alpha = if (isEnabled) 1f else 0.38f
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
                .alpha(alpha)
        )
        Text(
            text = word,
            Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_small))
                .weight(1f)
                .pointerInput(Unit) {
                    if (isEnabled) {
                        detectTapGestures(
                            onDoubleTap = { onItemReset() }
                        )
                    }
                }
                .alpha(alpha),
        )
        SpellingRadioGroup(
            selected = wordStatus,
            setSelected = onItemSelected,
            rbContentType = rbContentType,
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
    onItemReset: (Int) -> Unit = {},
    rbContentType: RadioButtonContentType,
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
                    onItemReset = { onItemReset(index) },
                    rbContentType = rbContentType,
                    isEnabled = isEnabled,
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

private fun getRadioButtonType(windowSize: WindowWidthSizeClass): RadioButtonContentType {
    return when (windowSize) {
        WindowWidthSizeClass.Compact -> RadioButtonContentType.BUTTONS_ONLY
        WindowWidthSizeClass.Medium -> RadioButtonContentType.BUTTONS_AND_SHORT
        else -> RadioButtonContentType.BUTTONS_AND_LONG
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 720, heightDp = 1480)  //Z.
@Preview(showBackground = true, widthDp = 1080, heightDp = 2000) //T.
@Preview(showBackground = true, widthDp = 800, heightDp = 1280)  //E.
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingItemPreview() {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    val rbContentType = getRadioButtonType(WindowSizeClass.calculateFromSize(size).widthSizeClass)
    HomeLearningTheme {
        Scaffold {
            SpellingItem(
                index = 17,
                word = "successfully",
                wordStatus = WordStatus.CORRECT,
                onItemSelected = {},
                rbContentType = rbContentType,
                onItemReset = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingItemDisabledPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingItem(
                index = 0,
                word = "successfully",
                wordStatus = WordStatus.CORRECT,
                onItemSelected = {},
                onItemReset = {},
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                isEnabled = false,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingItemsPreview() {
    val spelling1 = SpellingWord(
        word = "successfully",
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
        Scaffold {
            SpellingItems(
                words = words,
                onValueChange = { _, _ -> run {} },
                onItemReset = {},
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                modifier = Modifier.padding(it)
            )
        }
    }
}
