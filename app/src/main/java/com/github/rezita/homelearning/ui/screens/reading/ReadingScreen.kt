package com.github.rezita.homelearning.ui.screens.reading

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.ReadingRule
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.uiState.UIState
import com.github.rezita.homelearning.ui.viewmodels.ReadingViewModel
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplay
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.ReadingTopAppBar
import com.github.rezita.homelearning.utils.getForBlackDisplay
import com.github.rezita.homelearning.utils.getForColorDisplay
import com.github.rezita.homelearning.utils.getOutlineText


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadingScreen(viewModel: ReadingViewModel, modifier: Modifier = Modifier) {
    val readingState by viewModel.readingUIState.collectAsState()
    val configuration = LocalConfiguration.current
    Scaffold(
        topBar = {
            ReadingTopAppBar(readingState.state,
                { value -> viewModel.setColorDisplay(value) },
                viewModel.isColourDisplay
            )
        }
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> ErrorDisplay(
                message = "Not in portrait mode",
                callback = { viewModel.load() })

            else -> {
                when (readingState.state) {
                    UIState.LOADING -> LoadingProgressBar()
                    UIState.SUCCESS -> ReadingWordItems(
                        words = readingState.words,
                        isColorDisplay = viewModel.isColourDisplay,
                        modifier = Modifier.padding(it)
                    )

                    else -> ErrorDisplay(
                        message = readingState.message,
                        callback = { viewModel.load() })
                }
            }
        }
    }
}

@Composable
fun ReadingWordItems(
    words: List<ReadingWord>,
    isColorDisplay: Boolean,
    modifier: Modifier = Modifier
) {
    val nrOfPages = words.size
    val state = rememberPagerState { nrOfPages }
    HorizontalPager(
        state = state,
        modifier = modifier
            .fillMaxWidth()
    ) { page ->
        ReadingWordItem(
            word = words[page],
            isColorDisplay = isColorDisplay,
            currentPageNr = page,
            nrOfPages = nrOfPages
        )
    }
}

@Composable
fun ReadingWordItem(
    word: ReadingWord,
    isColorDisplay: Boolean,
    currentPageNr: Int,
    nrOfPages: Int,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(12.dp),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            TextDisplay(word = word, isDecorated = isColorDisplay)
        }
        Column(
            modifier = Modifier
                .padding(end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.reading_counter, currentPageNr + 1, nrOfPages),
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Right,
            )
        }
    }
}

@Composable
fun TextDisplay(word: ReadingWord, isDecorated: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = if (isDecorated) word.getForColorDisplay() else word.getForBlackDisplay(),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            text = word.getOutlineText(),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
        )

    }
}

@Preview
@Composable
fun ReadingWordItemDisplayBlackReview() {
    val rule1 = ReadingRule(word = "she", subWord = "sh", ruleName = "ul")
    val rule2 = ReadingRule(word = "she", subWord = "e", ruleName = "green")
    val readingWord = ReadingWord(
        word = "she",
        comment = "tricky words",
        rules = listOf(rule1, rule2),
        category = "phase2"
    )
    HomeLearningTheme {
        ReadingWordItem(readingWord, false, 3, 107)
    }
}

@Preview
@Composable
fun ReadingWordItemDisplayColorfulReview() {
    val rule1 = ReadingRule(word = "she", subWord = "sh", ruleName = "ul")
    val rule2 = ReadingRule(word = "she", subWord = "e", ruleName = "green")
    val readingWord = ReadingWord(
        word = "she",
        comment = "tricky words",
        rules = listOf(rule1, rule2),
        category = "phase2"
    )
    HomeLearningTheme {
        ReadingWordItem(readingWord, true, 3, 107)
    }
}

@Preview
@Composable
fun ReadingWordItemsReview() {
    val rule1 = ReadingRule(word = "she", subWord = "sh", ruleName = "ul")
    val rule2 = ReadingRule(word = "she", subWord = "e", ruleName = "green")
    val readingWord1 = ReadingWord(
        word = "she",
        comment = "tricky words",
        rules = listOf(rule1, rule2),
        category = "phase2"
    )

    val readingWord2 = ReadingWord(
        word = "help",
        comment = "HFW",
        rules = emptyList(),
        category = "phase3"
    )

    val rule3 = ReadingRule(word = "you", subWord = "ou", ruleName = "blue")
    val readingWord3 = ReadingWord(
        word = "you",
        comment = "tricky",
        rules = listOf(rule3),
        category = "phase2"
    )

    val words = listOf(readingWord1, readingWord2, readingWord3)
    HomeLearningTheme {
        ReadingWordItems(words, true)
    }
}