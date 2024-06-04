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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
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
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.size.WidthSizeBasedValue
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.theme.balsamiq
import com.github.rezita.homelearning.ui.viewmodels.ReadingViewModel
import com.github.rezita.homelearning.utils.getDecorated
import com.github.rezita.homelearning.utils.getOutlineText
import com.github.rezita.homelearning.utils.getUndecorated
import com.github.rezita.homelearning.utils.toDp
import kotlin.math.min
import kotlin.math.roundToInt

const val MIN_FONT_SIZE = 14

@Composable
fun ReadingScreen(
    sheetAction: SheetAction,
    viewModel: ReadingViewModel = viewModel(
        factory = ReadingViewModel.ReadingWordViewModelFactory(
            sheetAction
        )
    ),
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    windowSize: HomeLearningWindowSizeClass,
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
                    canNavigateBack = canNavigateBack,
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
    windowSize: HomeLearningWindowSizeClass,
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
    windowSize: HomeLearningWindowSizeClass,
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
    windowSize: HomeLearningWindowSizeClass,
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
    windowSize: HomeLearningWindowSizeClass,
    word: ReadingWord,
    isDecorated: Boolean,
    modifier: Modifier = Modifier
) {
    val maxFontSizes = WidthSizeBasedValue(96.sp, 128.sp, 156.sp, 196.sp, 256.sp)
    var size by remember { mutableStateOf(IntSize.Zero) }
    val textMeasurer = rememberTextMeasurer()

    val basicTextStyle = TextStyle(
        fontSize = maxFontSizes(windowSize.widthClassType2),
        fontWeight = FontWeight.Normal,
        fontFamily = balsamiq,
    )

    val fontSize = getFontSize(
        text = word.word,
        textMeasurer = textMeasurer,
        textStyle = basicTextStyle,
        maxWidth = size.width
    )

    val color = MaterialTheme.colorScheme.onSurface
    val textStyle = basicTextStyle.copy(fontSize = fontSize)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        contentAlignment = Alignment.Center
    )
    {
        val text = if (isDecorated) word.getDecorated(color) else word.getUndecorated(color)
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size = it },

            textAlign = TextAlign.Center,
            style = textStyle,
            maxLines = 1,
        )
        Text(
            text = word.getOutlineText(color),
            textAlign = TextAlign.Center,
            style = textStyle,
            maxLines = 1,
        )
    }
}

@Composable
fun getFontSize(
    text: String,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    maxWidth: Int
): TextUnit {
    if (maxWidth == 0) return textStyle.fontSize

    val actualTextWidth = textMeasurer.measure(text, textStyle).size.width
    //the text fits
    if (actualTextWidth <= maxWidth) return textStyle.fontSize

    val newSize = min(
        (maxWidth * textStyle.fontSize.toDp().value / actualTextWidth).roundToInt(),
        textStyle.fontSize.toDp().value.toInt() - 1
    )
    if (newSize.sp <= MIN_FONT_SIZE.sp) return MIN_FONT_SIZE.sp

    return getFontSize(
        text = text,
        textMeasurer = textMeasurer,
        textStyle = textStyle.copy(fontSize = newSize.sp),
        maxWidth = maxWidth
    )
}

@Preview(
    name = "small min 1f",
    showBackground = true,
    device = "spec:shape=Normal,width=600,height=320,unit=dp,dpi=160",
    showSystemUi = false
)
@Preview(
    name = "small min 2f",
    showBackground = true,
    device = "spec:shape=Normal,width=600,height=320,unit=dp,dpi=160",
    fontScale = 2f,
    showSystemUi = false
)
@Composable
fun ReadingWordItemDisplayLongWord(
) {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    val windowSize =
        HomeLearningWindowSizeClass.calculateFromSize(size)
    val readingWord = ReadingWord(
        word = "children",
        comment = "other",
        rules = emptyList(),
        category = "phase2"
    )
    HomeLearningTheme {
        Scaffold {
            ReadingWordItem(
                windowSize = windowSize,
                word = readingWord,
                isColorDisplay = false,
                currentPageNr = 3,
                nrOfPages = 107,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 899, heightDp = 390)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 899,
    heightDp = 390
)
@Composable
fun ReadingWordItemDisplay(
    @PreviewParameter(ColorDisplayParameterProvider::class) isColorDisplay: Boolean
) {
    val rule1 = ReadingRule(word = "she", pattern = "sh", ruleName = "ul")
    val rule2 = ReadingRule(word = "she", pattern = "e", ruleName = "green")
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
            windowSize = HomeLearningWindowSizeClass.calculateFromSize(size),
            word = readingWord,
            isColorDisplay = isColorDisplay,
            currentPageNr = 3,
            nrOfPages = 107
        )
    }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 800)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 600,
    heightDp = 800
)
@Composable
fun ReadingWordItemDisplayManyRules(
    @PreviewParameter(ColorDisplayParameterProvider::class) isColorDisplay: Boolean
) {
    val rule1 = ReadingRule(word = "agIfkvwitlhnmrube", pattern = "a", ruleName = "ul")
    val rule2 = ReadingRule(word = "agIfkvwitlhnmrube", pattern = "g", ruleName = "white")
    val rule3 = ReadingRule(word = "agIfkvwitlhnmrube", pattern = "I", ruleName = "yellow")
    val rule4 = ReadingRule(word = "agIfkvwitlhnmrube", pattern = "kv", ruleName = "red")
    val rule5 = ReadingRule(word = "agIfkvwitlhnmrube", pattern = "wi", ruleName = "blue")
    val rule6 = ReadingRule(word = "agIfkvwitlhnmrube", pattern = "tl", ruleName = "pink")
    val rule7 = ReadingRule(word = "agIfkvwitlhnmrube", pattern = "hn", ruleName = "green")
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
            windowSize = HomeLearningWindowSizeClass.calculateFromSize(size),
            word = readingWord,
            isColorDisplay = isColorDisplay,
            currentPageNr = 3,
            nrOfPages = 107
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ReadingWordItemsReview(
    @PreviewParameter(ColorDisplayParameterProvider::class) isColorDisplay: Boolean
) {
    val rule1 = ReadingRule(word = "she", pattern = "sh", ruleName = "ul")
    val rule2 = ReadingRule(word = "she", pattern = "e", ruleName = "green")
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

    val rule3 = ReadingRule(word = "you", pattern = "ou", ruleName = "blue")
    val readingWord3 = ReadingWord(
        word = "you",
        comment = "tricky",
        rules = listOf(rule3),
        category = "phase2"
    )

    val rule4 = ReadingRule(word = "I", pattern = "I", ruleName = "yellow")
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
            windowSize = HomeLearningWindowSizeClass.calculateFromSize(size),
            words,
            isColorDisplay = isColorDisplay,
        )
    }
}