package com.github.rezita.homelearning.model

import org.json.JSONException
import org.json.JSONObject

class SpellingWord(var word: String) {
    var status = WordStatus.UNCHECKED
    var category: String = ""
    var comment: String = ""

    private val _jsonWord = "word"
    private val _jsonCategory = "category"
    private val _jsonComment = "comment"
    private val _jsonResult = "result"

    constructor(word: String, category: String, comment: String): this(word){
        this.category = category
        this.comment = comment
    }

    fun changeStatus(value: WordStatus){
        status =  value
    }

    fun isChanged(): Boolean{
        return (status != WordStatus.UNCHECKED)
    }

    @Throws(JSONException::class)
    fun convertToJSON() : JSONObject {
        val jsonObj = JSONObject()
        jsonObj.put(_jsonWord, word)
        jsonObj.put(_jsonCategory, category)
        jsonObj.put(_jsonComment, comment)
        jsonObj.put(_jsonResult, status.value)
        return jsonObj
    }

    fun changeWord(word: String, comment: String, category: String) {
        this.word = word
        this.comment = comment
        this.category = category
    }
}