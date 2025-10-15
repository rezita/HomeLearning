package com.github.rezita.homelearning.ui.screens.upload.common

const val MAX_NR_OF_WORDS = 10
const val MAX_WORD_LENGTH = 30
const val MAX_WORD_LENGTH_SP = 75
const val MAX_COMMENT_LENGTH = 25
const val RESPONSE_SEPARATOR = ","
const val RESPONSE_INNER_SEPARATOR = ":"

val wordPattern = Regex("^[a-zA-Z][a-zA-Z\\s'-()]{1,35}")
val commentPattern = Regex("[\\w\\s-']{1,35}")

