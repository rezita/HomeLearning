package com.github.rezita.homelearning.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.github.rezita.homelearning.model.ReadingRule
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.model.consonants
import com.github.rezita.homelearning.model.specialChars


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
            } else if (!rule.isSplitDigraph()) {
                val startIndex = word.indexOf(rule.pattern, 0, false)
                if (startIndex != -1) {
                    if (rule.isUnderline()) {
                        addStyle(
                            style = SpanStyle(textDecoration = TextDecoration.Underline),
                            start = startIndex,
                            end = startIndex + rule.pattern.length
                        )
                    } else {
                        addStyle(
                            style = SpanStyle(color = Color(rule.getRuleColor())),
                            start = startIndex,
                            end = startIndex + rule.pattern.length
                        )
                    }
                }
            } else {
                //split digraph eg. i-e at the end of anticlockwise
                val startIndex = getPatternIndex(word, rule)
                if (startIndex != -1) {
                    val color = Color(rule.getRuleColor())
                    addStyle(
                        style = SpanStyle(color = color),
                        start = startIndex,
                        end = startIndex + 1
                    )
                    addStyle(
                        style = SpanStyle(color = color),
                        start = startIndex + 2,
                        end = startIndex + 3
                    )
                }
            }
        }
    }
    return annotated
}

fun ReadingWord.getOutlineText(color: Color): AnnotatedString = buildAnnotatedString {
    append(word)
    addStyle(
        style = SpanStyle(
            color = color,
            drawStyle = Stroke(
                width = 3f,
                join = StrokeJoin.Round
            )
        ),
        start = 0,
        end = word.length,
    )
}

fun ReadingRule.patternToRegex(): Regex {
    val specChar = specialChars.find { pattern.contains(it) }.toString()
    return pattern.replace(specChar, consonants).toRegex()
}

fun getPatternIndex(word: String, rule: ReadingRule): Int {
    return word.indexOf(rule.patternToRegex())
}

fun CharSequence.indexOf(pattern: Regex, startIndex: Int = 0): Int {
    val matchResult = pattern.find(this, startIndex)
    return matchResult?.range?.first ?: -1

}