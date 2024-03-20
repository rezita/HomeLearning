package com.github.rezita.homelearning.utils

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.SEPARATOR
import com.github.rezita.homelearning.model.SOLUTION_SEPARATOR
import com.github.rezita.homelearning.model.SPACE
import com.github.rezita.homelearning.model.WordStatus


fun FillInSentence.getSeparatorIndex(): Int{
    return sentence.indexOfOrZero(SEPARATOR)
}

private fun FillInSentence.getWithSuggestionsAndSeparatorReplaced(replacement: String): String {
    val sentenceReplaced = sentence.replace(SEPARATOR, replacement)
    return "$sentenceReplaced ($suggestion)"
}

fun FillInSentence.getForDisplay(): String {
    return getWithSuggestionsAndSeparatorReplaced(SPACE)
}

fun FillInSentence.getWithResult(): SpannableStringBuilder {
    val separatorIndex = sentence.indexOfOrZero(SEPARATOR)
    val result = SpannableStringBuilder(getWithSuggestionsAndSeparatorReplaced(""))
    val solutionsString = solutions.joinWithSeparator(SOLUTION_SEPARATOR)
    when (status) {
        WordStatus.UNCHECKED -> {
            result.insert(separatorIndex, solutionsString)
            result.setSpan(
                ForegroundColorSpan(Color.RED),
                separatorIndex,
                separatorIndex + solutionsString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        WordStatus.CORRECT -> {
            result.insert(separatorIndex, answer)
            result.setSpan(
                ForegroundColorSpan(Color.GREEN),
                separatorIndex,
                separatorIndex + answer.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        WordStatus.INCORRECT -> {
            result.insert(separatorIndex, answer)
            result.setSpan(
                ForegroundColorSpan(Color.RED),
                separatorIndex,
                separatorIndex + answer.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )

            result.setSpan(
                StrikethroughSpan(),
                separatorIndex,
                separatorIndex + answer.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            //add extra space between answer and solutions
            result.insert(separatorIndex + answer.length, " ")

            result.insert(separatorIndex + answer.length + 1, solutionsString)
            result.setSpan(
                ForegroundColorSpan(Color.GREEN),
                separatorIndex + answer.length + 1,
                separatorIndex + answer.length + solutionsString.length + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    return result
}