package com.github.rezita.homelearning.model

import kotlinx.serialization.Serializable

data class ReadingWord(
    val word: String,
    val category: String,
    val comment: String,
    val rules: List<ReadingRule>
)

@Serializable
data class ApiReadingWord(
    val word: String,
    val category: String,
    val comment: String,
    val rule: String
)