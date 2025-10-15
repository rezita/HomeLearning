package com.github.rezita.homelearning.ui.screens.upload.common

import android.util.Log
import com.github.rezita.homelearning.model.Uploadable
import com.github.rezita.homelearning.utils.toListBySeparator

@Suppress("UNCHECKED_CAST")
fun <T : Uploadable> parseResponse(response: String, list: List<T>): List<Pair<T, String>> {
    Log.i("Response", response)

    val result: ArrayList<Pair<T, String>> = ArrayList()
    val parsedResponse = response
        .toListBySeparator(RESPONSE_SEPARATOR)
        .mapNotNull { part ->
            val trimmed = part.trim()
            if (trimmed.isEmpty()) return@mapNotNull null
            val (key, value ) = trimmed.split(RESPONSE_INNER_SEPARATOR, limit = 2).map { it.trim() }
            if (key.isNotEmpty() && value.isNotEmpty()) key to value else null
        }

    for (word in list){
        val wordResponse = parsedResponse.find { it.first == word.getBaseProperty() }
        if(wordResponse != null){
            result.add(word to wordResponse.second)
        }else {
            result.add(word to "Error")
        }
    }

    Log.i("Result", result.toString())
    return result
}

private fun <T : Uploadable> findWordInList(word: String, list: List<T>): T? {
    return list.find { it.getBaseProperty() == word }
}

fun isValidText(text: String, pattern: Regex): Boolean {
    return pattern.matches(text)
}

