package com.github.rezita.homelearning.model

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import org.json.JSONException
import org.json.JSONObject

class FillInSentence(private val sentence: String, private val suggestion: String, private val solutions: ArrayList<String>) {
    private val _jsonSuggestion = "suggestion"
    private val _jsonSentence = "sentence"
    private val _jsonResult = "result"

    private val _separator = "$£"
    private val _spaces = " "
    
    var status = WordStatus.UNCHECKED

    private var tense = ""

    var answer = ""
        set(value) {
            field = value
            status = when(value) {
                "" -> WordStatus.UNCHECKED
                in solutions -> WordStatus.CORRECT
                else -> WordStatus.INCORRECT
            }

        }

    constructor(sentence: String, suggestion: String, solutions: ArrayList<String>, tense: String) : this(
        sentence,
        suggestion,
        solutions
    ) {
        this.tense = tense
    }

    fun changeStatus(value: WordStatus) {
        status = value
    }

    fun isChanged(): Boolean {
        return (status != WordStatus.UNCHECKED)
    }

    fun getSeparatorIndex(): Int {
        val index = sentence.indexOf(this._separator)
        return if (index > 0) index else 0
    }

    private fun getSentenceWithSuggestion(): String {
        return "$sentence (${this.suggestion})"
    }

    fun getSentenceWithSpaceAndSuggestion(): String {
        val sentenceWithSpace = sentence.replace(_separator, _spaces)
        return "$sentenceWithSpace (${this.suggestion})"
    }

    private fun getSolutionsAsString(): String {
        return solutions.joinToString(separator = " / ")
    }

    fun getAsResult():SpannableStringBuilder{
        val separatorIndex = getSeparatorIndex()
        val result = SpannableStringBuilder(getSentenceWithSuggestion().replace(_separator, ""))
        val solutions = getSolutionsAsString()
        when (status) {
            WordStatus.UNCHECKED -> {
                result.insert(separatorIndex, solutions)
                result.setSpan(
                    ForegroundColorSpan(Color.RED),
                separatorIndex,
                separatorIndex + solutions.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            }

            WordStatus.CORRECT -> {
                result.insert(separatorIndex, answer)
                result.setSpan(
                    ForegroundColorSpan(Color.GREEN),
                    separatorIndex,
                    separatorIndex + answer.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            WordStatus.INCORRECT -> {
                result.insert(separatorIndex, answer)
                result.setSpan(
                    ForegroundColorSpan(Color.RED),
                    separatorIndex,
                    separatorIndex + answer.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

                result.setSpan(
                    StrikethroughSpan(),
                    separatorIndex,
                    separatorIndex + answer.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                //add extra space between answer and solutions
                result.insert(separatorIndex + answer.length, " ")

                result.insert(separatorIndex + answer.length + 1, solutions)
                result.setSpan(
                    ForegroundColorSpan(Color.GREEN),
                    separatorIndex + answer.length + 1,
                    separatorIndex + answer.length + solutions.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return result
    }

    @Throws(JSONException::class)
    fun convertToJSON() : JSONObject {
        val jsonObj = JSONObject()
        jsonObj.put(_jsonSuggestion, suggestion)
        jsonObj.put(_jsonSentence, sentence)
        jsonObj.put(_jsonResult, status.value)
        return jsonObj
    }
}
