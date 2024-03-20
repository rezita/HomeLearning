package com.github.rezita.homelearning.model

const val SEPARATOR = "$£"
const val SPACES = " "
const val SOLUTION_SEPARATOR = " / "

data class FillInSentence(
    val sentence: String,
    val suggestion: String,
    val solutions: ArrayList<String>,
    val status: WordStatus = WordStatus.UNCHECKED,
    val answer: String = "",
    val tense: String = "") {

    fun isChanged(): Boolean {
        return (status != WordStatus.UNCHECKED)
    }
}
