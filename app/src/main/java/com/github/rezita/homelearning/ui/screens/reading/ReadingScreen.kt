package com.github.rezita.homelearning.ui.screens.reading

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.ReadingRule
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithIcon
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.theme.balsamiq
import com.github.rezita.homelearning.ui.viewmodels.ReadingViewModel
import com.github.rezita.homelearning.utils.getDecorated
import com.github.rezita.homelearning.utils.getOutlineText
import com.github.rezita.homelearning.utils.getUndecorated


@Composable
fun ReadingScreen(
    sheetAction: SheetAction,
    viewModel: ReadingViewModel = viewModel(
        factory = ReadingViewModel.ReadingWordViewModelFactory(
            sheetAction
        )
    ),
    navigateUp: () -> Unit,
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val readingState by viewModel.readingUIState.collectAsState()
    var isTopAppBarShown by remember {
        mutableStateOf(true)
    }
    val configuration = LocalConfiguration.current

    Scaffold(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = { _ ->
                    isTopAppBarShown = !isTopAppBarShown
                }
            )
        },
        topBar = {
            if (isTopAppBarShown) {
                ReadingTopAppBar(
                    state = readingState,
                    navigateUp = navigateUp,
                    callback = { value -> viewModel.setColorDisplay(value) },
                    isColorDisplay = viewModel.isColourDisplay
                )
            }
        }
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                ErrorDisplayWithIcon(
                    message = stringResource(id = R.string.msg_turn_landscape_mode),
                    iconSource = R.drawable.screen_rotation_24px,
                    modifier = modifier
                        .padding(it)
                )

            else ->
                ReadingContent(
                    windowSize = windowSize,
                    state = readingState,
                    isColorDisplay = viewModel.isColourDisplay,
                    onLoadCallback = { viewModel.load() },
                    modifier = modifier.padding(it)
                )
        }
    }
}


@Composable
fun ReadingContent(
    windowSize: WindowSizeClass,
    state: ReadingUiState,
    isColorDisplay: Boolean = false,
    onLoadCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is ReadingUiState.Loading -> LoadingProgressBar(modifier = modifier)
        is ReadingUiState.Downloaded -> ReadingWordItems(
            windowSize = windowSize,
            words = state.words,
            isColorDisplay = isColorDisplay,
            modifier = modifier
        )

        is ReadingUiState.LoadingError -> ErrorDisplayInColumn(
            message = stringResource(id = state.errorMessage),
            callback = onLoadCallback,
            modifier = modifier
        )
    }

}

@Composable
fun ReadingWordItems(
    windowSize: WindowSizeClass,
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
            windowSize = windowSize,
            word = words[page],
            isColorDisplay = isColorDisplay,
            currentPageNr = page,
            nrOfPages = nrOfPages
        )
    }
}

@Composable
fun ReadingWordItem(
    windowSize: WindowSizeClass,
    word: ReadingWord,
    isColorDisplay: Boolean,
    currentPageNr: Int,
    nrOfPages: Int,
    modifier: Modifier = Modifier
) {
    Card(
        //elevation = CardDefaults.cardElevation(12.dp),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
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
            TextDisplay(windowSize = windowSize, word = word, isDecorated = isColorDisplay)
        }
        Column(
            modifier = Modifier
                .padding(
                    end = dimensionResource(id = R.dimen.padding_medium),
                    bottom = dimensionResource(id = R.dimen.padding_medium)
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.reading_counter, currentPageNr + 1, nrOfPages),
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Right,
            )
        }
    }
}

@Composable
fun TextDisplay(
    windowSize: WindowSizeClass,
    word: ReadingWord,
    isDecorated: Boolean,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.onSurface
    val fontSize = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Expanded ->
            when (windowSize.heightSizeClass) {
                WindowHeightSizeClass.Compact -> 128.sp
                else -> 256.sp
            }

        else -> {
            96.sp
        }
    }

    val textStyle = androidx.compose.ui.text.TextStyle(
        fontFamily = balsamiq,
        fontWeight = FontWeight.Normal,
        fontSize = fontSize
    )
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = if (isDecorated) word.getDecorated(color) else word.getUndecorated(
                color
            ),
            style = textStyle,
            textAlign = TextAlign.Center,
        )
        Text(
            text = word.getOutlineText(),
            textAlign = TextAlign.Center,
            style = textStyle,
        )

    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 720, heightDp = 1480)     //Z.
@Preview(showBackground = true, widthDp = 1480, heightDp = 720)     //Z.
@Preview(showBackground = true, widthDp = 2000, heightDp = 1080)  //T.
@Preview(showBackground = true, widthDp = 1080, heightDp = 2000)  //T.
@Preview(showBackground = true, widthDp = 1280, heightDp = 800)  //E.
@Preview(showBackground = true, widthDp = 800, heightDp = 1280)  //E.
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 600,
    heightDp = 800
)
@Composable
fun ReadingWordItemDisplayLongWord() {
    val rule1 = ReadingRule(word = "children", subWord = "ch", ruleName = "ul")
    val readingWord = ReadingWord(
        word = "children",
        comment = "tricky words",
        rules = listOf(rule1),
        category = "phase2"
    )
    HomeLearningTheme {
        val configuration = LocalConfiguration.current
        val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
        ReadingWordItem(
            windowSize = WindowSizeClass.calculateFromSize(size),
            readingWord,
            false,
            3,
            107
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 800, heightDp = 1280)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
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
        val configuration = LocalConfiguration.current
        val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
        ReadingWordItem(
            windowSize = WindowSizeClass.calculateFromSize(size),
            readingWord,
            false,
            3,
            107
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 600, heightDp = 800)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 600,
    heightDp = 800
)
@Composable
fun ReadingWordItemDisplayColorfulReview() {
    val rule1 = ReadingRule(word = "agIfkvwitlhnmrube", subWord = "a", ruleName = "ul")
    val rule2 = ReadingRule(word = "agIfkvwitlhnmrube", subWord = "g", ruleName = "white")
    val rule3 = ReadingRule(word = "agIfkvwitlhnmrube", subWord = "I", ruleName = "yellow")
    val rule4 = ReadingRule(word = "agIfkvwitlhnmrube", subWord = "kv", ruleName = "red")
    val rule5 = ReadingRule(word = "agIfkvwitlhnmrube", subWord = "wi", ruleName = "blue")
    val rule6 = ReadingRule(word = "agIfkvwitlhnmrube", subWord = "tl", ruleName = "pink")
    val rule7 = ReadingRule(word = "agIfkvwitlhnmrube", subWord = "hn", ruleName = "green")
    val readingWord = ReadingWord(
        word = "agIfkvwitlhnmrube",
        comment = "tricky words",
        rules = listOf(rule1, rule2, rule3, rule4, rule5, rule6, rule7),
        category = "phase2"
    )
    HomeLearningTheme {
        val configuration = LocalConfiguration.current
        val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
        ReadingWordItem(
            windowSize = WindowSizeClass.calculateFromSize(size),
            readingWord,
            true,
            3,
            107
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
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

    val rule4 = ReadingRule(word = "I", subWord = "I", ruleName = "yellow")
    val readingWord4 = ReadingWord(
        word = "I",
        comment = "tricky",
        rules = listOf(rule4),
        category = "phase2"
    )

    val words = listOf(readingWord4, readingWord2, readingWord3, readingWord1)
    HomeLearningTheme {
        val configuration = LocalConfiguration.current
        val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
        ReadingWordItems(
            windowSize = WindowSizeClass.calculateFromSize(size),
            words,
            isColorDisplay = true,
        )
    }
}