package com.github.rezita.homelearning.utils

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.model.SpellingWord
import org.json.JSONObject

class JSONSerializer {

    fun parseSpellingWords(jsonString: String): Either<RemoteError, ArrayList<SpellingWord>>  {
        val jsonObject = JSONObject(jsonString)
        return if (jsonObject.has("error")) {
            RemoteError(jsonObject.getString("error")).left()
        } else {
            readSpellingWords(jsonObject).right()
        }
    }

    private fun readSpellingWords(jsonObject: JSONObject): ArrayList<SpellingWord> {
        val words = ArrayList<SpellingWord>()
        val jsonArray = jsonObject.getJSONArray("items")

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val word = jsonObj.getString("word")
            val category = jsonObj.getString("category")
            val comment = jsonObj.getString("comment")
            val spellingWord = SpellingWord(word, category, comment)
            words.add(spellingWord)
        }
        return words
    }

    fun parseCategories(jsonString: String): Either<RemoteError, ArrayList<String>>  {
        val jsonObject = JSONObject(jsonString)
        return if (jsonObject.has("error")) {
            RemoteError(jsonObject.getString("error")).left()
        } else {
            readCategories(jsonObject).right()
        }
    }

    private fun readCategories(jsonObject: JSONObject): ArrayList<String> {
        val categories = ArrayList<String>()
        val jsonArray = jsonObject.getJSONArray("categories")
        for (i in 0 until jsonArray.length()) {
            categories.add(jsonArray[i].toString())
        }
        return  categories
    }

    fun parseReadingWords(jsonString: String): Either<RemoteError, ArrayList<ReadingWord>>  {
        val jsonObject = JSONObject(jsonString)
        return if (jsonObject.has("error")) {
            RemoteError(jsonObject.getString("error")).left()
        } else {
            readReadingWords(jsonObject).right()
        }
    }

    private fun readReadingWords(jsonObject: JSONObject): ArrayList<ReadingWord> {
        val words = ArrayList<ReadingWord>()
        val jsonArray = jsonObject.getJSONArray("items")

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val word = jsonObj.getString("word")
            val category = jsonObj.getString("category")
            val comment = jsonObj.getString("comment")
            val rules = jsonObj.getString("rule")

            val readingWord = ReadingWord(word, category, comment, rules)
            words.add(readingWord)
        }
        return words
    }

    fun parseSentences(jsonString: String): Either<RemoteError, ArrayList<FillInSentence>>  {
        val jsonObject = JSONObject(jsonString)
        return if (jsonObject.has("error")) {
            RemoteError(jsonObject.getString("error")).left()
        } else {
            readSentences(jsonObject).right()
        }
    }

    private fun readSentences(jsonObject: JSONObject): ArrayList<FillInSentence> {
        val solutionSeparator = "/"
        val sentences = ArrayList<FillInSentence>()
        val jsonArray = jsonObject.getJSONArray("items")

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val suggestion = jsonObj.getString("suggestion")
            val sentence = jsonObj.getString("sentence")
            val solution = jsonObj.getString("solution")

            val solutions = solution.split(solutionSeparator).map{ it.trim()} as ArrayList<String>

            val fillInSentence = FillInSentence(sentence, suggestion, solutions)
            sentences.add(fillInSentence)
        }
        return sentences
    }

}