package com.github.rezita.homelearning.ui.screens.reading

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.ReadingRule
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.reading.ReadingContent
import com.github.rezita.homelearning.ui.screens.common.reading.getBasicFontStyleFor
import com.github.rezita.homelearning.ui.screens.common.reading.getFontSize
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.utils.getDecorated
import com.github.rezita.homelearning.utils.getOutlineText
import com.github.rezita.homelearning.utils.getUndecorated

const val MIN_FONT_SIZE = 14

@Composable
fun EnglishReadingContent(
    windowSize: HomeLearningWindowSizeClass,
    state: ReadingUiState,
    onUserEvent: (ReadingUserEvent) -> Unit,
    modifier: Modifier = Modifier,
    isColorDisplay: Boolean = false,
) {
    when (state) {
        is ReadingUiState.Loading -> LoadingProgressBar(modifier = modifier)
        is ReadingUiState.Downloaded -> ReadingContent(
            nrOfPages = state.words.size,
            modifier = modifier,
            content = { index ->
                EnglishReadingItem(
                    windowSize = windowSize,
                    word = state.words[index],
                    isDecorated = isColorDisplay
                )
            }
        )

        is ReadingUiState.LoadingError -> ErrorDisplayInColumn(
            message = stringResource(id = state.errorMessage),
            callback = { onUserEvent(ReadingUserEvent.OnLoad) },
            modifier = modifier
        )
    }
}


@Composable
private fun EnglishReadingItem(
    windowSize: HomeLearningWindowSizeClass,
    word: ReadingWord,
    isDecorated: Boolean,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val textMeasurer = rememberTextMeasurer()

    val basicTextStyle = getBasicFontStyleFor(windowSize)

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

@Preview(
    name = "small min 1f",
    showBackground = true,
    device = "spec:width=600dp,height=320dp,dpi=160",
    showSystemUi = false
)
@Preview(
    name = "small min 2f",
    showBackground = true,
    device = "spec:width=600dp,height=320dp,dpi=160",
    fontScale = 2f,
    showSystemUi = false
)
@Composable
private fun ReadingWordItemDisplayLongWord(
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
            ReadingContent(
                nrOfPages = 107,
                modifier = Modifier.padding(it),
                content = {
                    EnglishReadingItem(
                        windowSize = windowSize,
                        word = readingWord,
                        isDecorated = false
                    )
                }
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
private fun ReadingWordItemDisplay(
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
        ReadingContent(
            nrOfPages = 107,
            content = {
                EnglishReadingItem(
                    windowSize = HomeLearningWindowSizeClass.calculateFromSize(size),
                    word = readingWord,
                    isDecorated = isColorDisplay
                )
            }
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
private fun ReadingWordItemDisplayManyRules(
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
        ReadingContent(
            nrOfPages = 107,
            content = {
                EnglishReadingItem(
                    windowSize = HomeLearningWindowSizeClass.calculateFromSize(size),
                    word = readingWord,
                    isDecorated = isColorDisplay
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ReadingWordItemsReview(
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
        ReadingContent(
            nrOfPages = words.size,
            content = { page ->
                EnglishReadingItem(
                    windowSize = HomeLearningWindowSizeClass.calculateFromSize(size),
                    word = words[page],
                    isDecorated = isColorDisplay
                )
            }
        )
    }
}