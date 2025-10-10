package com.github.rezita.homelearning.model

import com.github.rezita.homelearning.utils.simplify
import kotlinx.serialization.Serializable

data class SpanishWord(
    val wordEn: String,
    val wordSp: String,
    val isWeekWord: Boolean,
    val answer: String = "",
    val enToSp: Boolean = true//translate from English To Spanish
) {
    val status: WordStatus = when (answer.simplify().lowercase()) {
        "" -> {
            WordStatus.UNCHECKED
        }

        wordSp.simplify().lowercase() -> {
            if (enToSp) WordStatus.CORRECT else WordStatus.INCORRECT
        }

        wordEn.simplify().lowercase() -> {
            if (!enToSp) WordStatus.CORRECT else WordStatus.INCORRECT
        }

        else -> {
            WordStatus.INCORRECT
        }
    }

    val solution = if(enToSp) wordSp else wordEn
}


@Serializable
data class ApiSpanishWord(
    val en: String,
    val sp: String,
    val isWeekWord: Int = 0,
    val result: String = "0",
)