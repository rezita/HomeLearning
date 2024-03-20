package com.github.rezita.homelearning.model

data class SpellingWord(val word: String, val category: String, val comment: String, val status: WordStatus= WordStatus.UNCHECKED) {
    fun isChanged(): Boolean{
        return (status != WordStatus.UNCHECKED)
    }
}