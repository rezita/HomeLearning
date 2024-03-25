package com.github.rezita.homelearning.model

import kotlinx.serialization.Serializable

const val SEPARATOR = "$£"
const val SPACE = " "
const val SOLUTION_SEPARATOR = "/"

data class FillInSentence(
    val sentence: String,
    val suggestion: String,
    val solutions: List<String>,
    val answer: String = "",
    val tense: String = ""
) {
    val status: WordStatus = when (answer) {
        "" -> {
            WordStatus.UNCHECKED
        }

        in solutions -> {
            WordStatus.CORRECT
        }

        else -> {
            WordStatus.INCORRECT
        }
    }
}

@Serializable
data class ApiFillInSentence(
    val sentence: String,
    val suggestion: String,
    val solution: String,
    val answer: String = "",
    val tense: String = "",
    val result: String = "0"
)
