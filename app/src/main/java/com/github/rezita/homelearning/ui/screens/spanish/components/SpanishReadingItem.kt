package com.github.rezita.homelearning.ui.screens.spanish.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.ui.screens.common.reading.ReadingContent
import com.github.rezita.homelearning.ui.screens.common.reading.getBasicFontStyleForSpanish
import com.github.rezita.homelearning.ui.screens.common.reading.getBasicFontStyleForTranslate
import com.github.rezita.homelearning.ui.screens.common.reading.getFontSize
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpanishReadingItem(
    windowSize: HomeLearningWindowSizeClass,
    word: SpanishWord,
    showTranslate: Boolean,
    onSpeakerClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val textMeasurer = rememberTextMeasurer()

    val basicTextStyle = getBasicFontStyleForSpanish(windowSize)
    val basicFontStyleForTranslate = getBasicFontStyleForTranslate(windowSize)

    val text = if (word.enToSp) word.wordEn else word.wordSp
    val translateText = if (showTranslate) {
        if (word.enToSp) word.wordSp else word.wordEn
    } else ""

    val fontSize = getFontSize(
        text = text,
        textMeasurer = textMeasurer,
        textStyle = basicTextStyle,
        maxWidth = size.width
    )

    val textStyle = basicTextStyle.copy(fontSize = fontSize)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = modifier.weight(1f))
        SimpleTextWithSpeaker(
            text = text,
            showSpeaker = !word.enToSp,
            style = textStyle,
            onSpeakerClicked = onSpeakerClicked,
        )
        Spacer(modifier = modifier.weight(0.5f))
        SimpleTextWithSpeaker(
            text = translateText,
            showSpeaker = word.enToSp && showTranslate,
            style = basicFontStyleForTranslate,
            onSpeakerClicked = onSpeakerClicked,
        )
    }
}

@Preview(
    name = "small min 1f",
    showBackground = true,
    device = "spec:width=600dp,height=320dp,dpi=160",
    showSystemUi = false
)
@Composable
private fun SpanishWordItemDisplayLongWordPreview_showTranslate(
) {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    val windowSize =
        HomeLearningWindowSizeClass.calculateFromSize(size)
    val word = SpanishWord(
        wordEn = "the computer",
        wordSp = "la computadora",
        comment = "with comment",
        answer = "el compoter",
        isWeekWord = false,
        enToSp = false,
    )

    HomeLearningTheme {
        Scaffold {
            ReadingContent(
                nrOfPages = 107,
                modifier = Modifier.padding(it),
                content = {
                    SpanishReadingItem(
                        windowSize = windowSize,
                        word = word,
                        showTranslate = true,
                        onSpeakerClicked = {}
                    )
                }
            )
        }
    }
}

@Preview(
    name = "small min 1f",
    showBackground = true,
    device = "spec:width=600dp,height=320dp,dpi=160",
    showSystemUi = false
)
@Composable
private fun SpanishWordItemDisplayLongWordPreview_hideTranslate(
) {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    val windowSize =
        HomeLearningWindowSizeClass.calculateFromSize(size)
    val word = SpanishWord(
        wordEn = "the computer",
        wordSp = "la computadora",
        comment = "with comment",
        answer = "el compoter",
        isWeekWord = false,
        enToSp = false,
    )

    HomeLearningTheme {
        Scaffold {
            ReadingContent(
                nrOfPages = 107,
                modifier = Modifier.padding(it),
                content = {
                    SpanishReadingItem(
                        windowSize = windowSize,
                        word = word,
                        showTranslate = false,
                        onSpeakerClicked = {}
                    )
                }
            )
        }
    }
}
