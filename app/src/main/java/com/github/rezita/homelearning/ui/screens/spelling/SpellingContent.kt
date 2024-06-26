package com.github.rezita.homelearning.ui.screens.spelling

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.utils.toDp
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpellingContent(
    state: SpellingUiState,
    onItemValueChange: (Int, WordStatus) -> Unit,
    onItemReset: (Int) -> Unit,
    onLoadCallback: () -> Unit,
    onSaveCallback: () -> Unit,
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
                onValueChange = onItemValueChange,
                onItemReset = onItemReset,
                rbContentType = rbContentType,
                modifier = modifier
            )
        }

        is SpellingUiState.LoadingError -> {
            LoadingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayInColumn(
                message = stringResource(id = state.errorMessage),
                callback = onLoadCallback,
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
                callback = onSaveCallback,
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
    if (rbContentType == RadioButtonContentType.BUTTONS_SECOND_LINE) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                SpellingTextWithNumber(
                    index = index,
                    word = word,
                    onItemReset = onItemReset,
                    isEnabled = isEnabled,
                    textModifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_big)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                SpellingRadioGroup(
                    selected = wordStatus,
                    setSelected = onItemSelected,
                    rbContentType = rbContentType,
                    isEnabled = isEnabled,
                )
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SpellingTextWithNumber(
                index = index,
                word = word,
                onItemReset = onItemReset,
                isEnabled = isEnabled,
                textModifier = Modifier.weight(1f)
            )
            SpellingRadioGroup(
                selected = wordStatus,
                setSelected = onItemSelected,
                rbContentType = rbContentType,
                isEnabled = isEnabled,
            )
        }
    }
}

@Composable
fun SpellingTextWithNumber(
    index: Int,
    word: String,
    onItemReset: () -> Unit,
    isEnabled: Boolean = true,
    textModifier: Modifier = Modifier
) {
    val alpha = if (isEnabled) 1f else 0.38f
    val fontScale = LocalDensity.current.fontScale
    val ordinalNumberWidth = (28.sp * fontScale)
    Text(
        text = getIndexPrefix(index),
        modifier = Modifier
            .width(ordinalNumberWidth.toDp())
            .alpha(alpha),
    )
    Text(
        text = word,
        modifier = textModifier
            .padding(end = dimensionResource(id = R.dimen.padding_small))
            .pointerInput(Unit) {
                if (isEnabled) {
                    detectTapGestures(
                        onDoubleTap = { onItemReset() }
                    )
                }
            }
            .alpha(alpha),
    )
}

private fun getIndexPrefix(index: Int): String {
    return "${index + 1}".padStart(1, ' ') + "."
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


@PreviewFontScale
@Composable
private fun SpellingItemSizePreview(
    @PreviewParameter(SpellingScreenSizeParameterProvider::class) screenSize: Pair<Int, Int>
) {
    val size = DpSize(
        screenSize.first.dp,
        screenSize.second.dp
    )

    val rbContentType =
        getRadioButtonType(
            HomeLearningWindowSizeClass.calculateFromSize(
                size
            ).widthClassType1
        )
    HomeLearningTheme {
        Scaffold(
            modifier = Modifier
                .width(screenSize.first.dp)
                .height(screenSize.second.dp)
        ) {
            Column {
                SpellingItem(
                    index = 17,
                    word = "anticlockwise",
                    wordStatus = WordStatus.CORRECT,
                    onItemSelected = {},
                    rbContentType = rbContentType,
                    onItemReset = {},
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun SpellingItemEnablePreview(
    @PreviewParameter(BooleanPreviewProvider::class) isEnabled: Boolean
) {
    HomeLearningTheme {
        Surface {
            SpellingItem(
                index = 0,
                word = "successfully",
                wordStatus = WordStatus.CORRECT,
                onItemSelected = {},
                onItemReset = {},
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                isEnabled = isEnabled,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SpellingItemsPreview(
    @PreviewParameter(BooleanPreviewProvider::class) isEnabled: Boolean
) {
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
        word = "anticlockwise",
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
    val spelling5 = SpellingWord(
        word = "this",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    val words = listOf(
        spelling1,
        spelling2,
        spelling3,
        spelling4,
        spelling5,
        spelling4,
        spelling4,
        spelling4,
        spelling4, spelling4,
        spelling4
    )
    HomeLearningTheme {
        Scaffold {
            SpellingItems(
                words = words,
                onValueChange = { _, _ -> run {} },
                onItemReset = {},
                isEnabled = isEnabled,
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                modifier = Modifier.padding(it)
            )
        }
    }
}