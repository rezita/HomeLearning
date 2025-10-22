package com.github.rezita.homelearning.ui.screens.common.reading

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.github.rezita.homelearning.ui.screens.reading.MIN_FONT_SIZE
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.size.WidthSizeBasedValue
import com.github.rezita.homelearning.ui.theme.balsamiq
import com.github.rezita.homelearning.utils.toDp
import kotlin.math.min
import kotlin.math.roundToInt

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

fun getBasicFontStyleFor(windowSize: HomeLearningWindowSizeClass): TextStyle {
    val maxFontSizes = WidthSizeBasedValue(96.sp, 128.sp, 156.sp, 196.sp, 256.sp)
    return TextStyle(
        fontSize = maxFontSizes(windowSize.widthClassType2),
        fontWeight = FontWeight.Normal,
        fontFamily = balsamiq,
    )
}

fun getBasicFontStyleForSpanish(windowSize: HomeLearningWindowSizeClass): TextStyle {
    val maxFontSizes = WidthSizeBasedValue(36.sp, 56.sp, 72.sp, 96.sp, 128.sp)
    return TextStyle(
        fontSize = maxFontSizes(windowSize.widthClassType2),
        fontWeight = FontWeight.Normal,
        fontFamily = balsamiq,
    )
}

fun getBasicFontStyleForTranslate(windowSize: HomeLearningWindowSizeClass): TextStyle {
    val maxFontSizes = WidthSizeBasedValue(18.sp, 24.sp, 36.sp, 48.sp, 64.sp)
    return TextStyle(
        fontSize = maxFontSizes(windowSize.widthClassType2),
        fontWeight = FontWeight.Normal,
        fontFamily = balsamiq,
    )
}
