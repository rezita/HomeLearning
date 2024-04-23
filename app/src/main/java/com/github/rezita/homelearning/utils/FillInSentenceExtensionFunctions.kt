package com.github.rezita.homelearning.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.SEPARATOR
import com.github.rezita.homelearning.model.SOLUTION_SEPARATOR
import com.github.rezita.homelearning.model.WordStatus

fun FillInSentence.splitBySparatorWithSuggestion(): Pair<String, String> {
    val index = this.sentence.indexOf(SEPARATOR)
    return when (index) {
        -1 -> Pair("", " ${this.sentence.trim()} ($suggestion)")
        0 -> Pair("", " ${this.sentence.substringAfter(SEPARATOR).trim()} ($suggestion)")
        this.sentence.length - SEPARATOR.length -> Pair(
            "${this.sentence.substringBefore(SEPARATOR).trim()} ", " ($suggestion)"
        )

        else -> Pair(
            "${this.sentence.substringBefore(SEPARATOR).trim()} ",
            " ${this.sentence.substringAfter(SEPARATOR).trim()} ($suggestion)"
        )
    }
}


fun FillInSentence.getSeparatorIndex(): Int {
    return sentence.indexOfOrZero(SEPARATOR)
}

private fun FillInSentence.getWithSuggestionsAndSeparatorReplaced(replacement: String): String {
    val sentenceReplaced = sentence.replace(SEPARATOR, replacement)
    return "$sentenceReplaced ($suggestion)"
}

fun FillInSentence.getWithResult(correctColor: Color, incorrectColor: Color): AnnotatedString {
    val (prefix, suffix) = splitBySparatorWithSuggestion()

    val annotated = buildAnnotatedString {
        append(prefix)
        append(getResultText(correctColor, incorrectColor))
        append(suffix)
    }
    return annotated
}

private fun FillInSentence.getResultText(correctColor: Color, incorrectColor: Color): AnnotatedString {
    val solutionsString = solutions.joinWithSeparator(SOLUTION_SEPARATOR)

    val annotated = buildAnnotatedString {
        when (status) {
            WordStatus.UNCHECKED -> {
                append(solutionsString)
                addStyle(
                    style = SpanStyle(color = incorrectColor),
                    start = 0,
                    end = solutionsString.length
                )
            }

            WordStatus.CORRECT -> {
                append(answer)
                addStyle(
                    style = SpanStyle(color = correctColor),
                    start = 0,
                    end = answer.length
                )

            }

            WordStatus.INCORRECT -> {
                append(answer)
                addStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = incorrectColor
                    ),
                    start = 0,
                    end = answer.length
                )
                //add extra space between answer and solutions
                append(" ")
                append(solutionsString)
                addStyle(
                    style = SpanStyle(color = correctColor),
                    start = answer.length + 1,
                    end = answer.length + solutionsString.length + 1
                )
            }
        }
    }

    return annotated
}