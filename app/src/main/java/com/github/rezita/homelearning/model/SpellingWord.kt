package com.github.rezita.homelearning.model

import org.json.JSONException
import org.json.JSONObject

data class SpellingWord(val word: String, val category: String, val comment: String, val status: WordStatus= WordStatus.UNCHECKED) {
    fun isChanged(): Boolean{
        return (status != WordStatus.UNCHECKED)
    }
}