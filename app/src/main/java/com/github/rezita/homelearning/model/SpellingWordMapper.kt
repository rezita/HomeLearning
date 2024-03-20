package com.github.rezita.homelearning.model

import org.json.JSONException
import org.json.JSONObject

private val _jsonWord = "word"
private val _jsonCategory = "category"
private val _jsonComment = "comment"
private val _jsonResult = "result"

@Throws(JSONException::class)
fun SpellingWord.convertToJSON() : JSONObject {
    val jsonObj = JSONObject()
    jsonObj.put(_jsonWord, word)
    jsonObj.put(_jsonCategory, category)
    jsonObj.put(_jsonComment, comment)
    jsonObj.put(_jsonResult, status.value)
    return jsonObj
}