package com.github.rezita.homelearning.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.github.rezita.homelearning.model.ReadingWord


fun ReadingWord.getUndecorated(baseColor: Color): AnnotatedString {
    return buildAnnotatedString {
        append(word)
        addStyle(SpanStyle(color = baseColor), start = 0, end = word.length)
    }
}

fun ReadingWord.getDecorated(baseColor: Color): AnnotatedString {
    val annotated = buildAnnotatedString {
        append(word)
        addStyle(SpanStyle(color = baseColor), start = 0, end = word.length)
        for (rule in rules) {
            if (rule.isSilentEndE()) {
                if (word[word.length - 1].toString().equals("e", ignoreCase = false)) {
                    addStyle(
                        style = SpanStyle(color = Color(rule.getRuleColor())),
                        start = word.length - 1,
                        end = word.length
                    )
                }
            } else {
                val startIndex = word.indexOf(rule.subWord, 0, false)
                if (startIndex != -1) {
                    if (rule.isUnderline()) {
                        addStyle(
                            style = SpanStyle(textDecoration = TextDecoration.Underline),
                            start = startIndex,
                            end = startIndex + rule.subWord.length
                        )
                    } else {
                        addStyle(
                            style = SpanStyle(color = Color(rule.getRuleColor())),
                            start = startIndex,
                            end = startIndex + rule.subWord.length
                        )
                    }
                }
            }
        }
    }
    return annotated
}

fun ReadingWord.getOutlineText(): AnnotatedString = buildAnnotatedString {
    append(word)
    addStyle(
        style = SpanStyle(
            color = Color.Black,
            drawStyle = Stroke(
                width = 3f,
                join = StrokeJoin.Round
            )
        ),
        start = 0,
        end = word.length,
    )
}