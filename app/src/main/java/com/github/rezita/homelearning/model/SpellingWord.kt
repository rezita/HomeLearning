package com.github.rezita.homelearning.model

import kotlinx.serialization.Serializable

data class SpellingWord(
    val word: String,
    val category: String,
    val comment: String,
    val status: WordStatus = WordStatus.UNCHECKED,
    val repeated: Boolean = false
) : Uploadable {
    override fun getDisplayedFields(): List<String> = listOf(word, category, comment)
    override fun getBaseProperty(): String = word
}

@Serializable
data class ApiSpellingWord(
    val word: String,
    val category: String,
    val comment: String,
    val result: String = "0",
    val repeat: Int = 0
)
