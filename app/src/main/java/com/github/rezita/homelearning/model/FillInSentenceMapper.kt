package com.github.rezita.homelearning.model

import org.json.JSONException
import org.json.JSONObject

private const val JSON_SUGGESTION = "suggestion"
private const val JSON_SENTENCE = "sentence"
private const val JSON_RESULT = "result"

@Throws(JSONException::class)
fun FillInSentence.convertToJSON() : JSONObject {
    val jsonObj = JSONObject()
    jsonObj.put(JSON_SUGGESTION, suggestion)
    jsonObj.put(JSON_SENTENCE, sentence)
    jsonObj.put(JSON_RESULT, status.value)
    return jsonObj
}