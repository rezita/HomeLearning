package com.github.rezita.homelearning.data

import android.util.Log
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.PostApiParameter
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.model.asAPISellingWord
import com.github.rezita.homelearning.model.asAPISentence
import com.github.rezita.homelearning.model.asApiSpanishWord
import com.github.rezita.homelearning.model.asFillInSentence
import com.github.rezita.homelearning.model.asReadingWord
import com.github.rezita.homelearning.model.asSpanishWord
import com.github.rezita.homelearning.model.asSpellingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.network.WordsApiService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

interface
WordRepository {
    suspend fun getReadingWords(): RepositoryResult<List<ReadingWord>>
    suspend fun getCEWWords(): RepositoryResult<List<ReadingWord>>

    suspend fun getIrregularVerbs(): RepositoryResult<List<FillInSentence>>
    suspend fun getHomophones(): RepositoryResult<List<FillInSentence>>

    suspend fun getErikSpellingWords(): RepositoryResult<List<SpellingWord>>
    suspend fun getMarkSpellingWords(): RepositoryResult<List<SpellingWord>>

    suspend fun getErikCategories(): RepositoryResult<List<String>>
    suspend fun getMarkCategories(): RepositoryResult<List<String>>

    suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): RepositoryResult<String>
    suspend fun updateHomophones(sentences: List<FillInSentence>): RepositoryResult<String>

    suspend fun updateErikSpellingWords(words: List<SpellingWord>): RepositoryResult<String>
    suspend fun updateMarkSpellingWords(words: List<SpellingWord>): RepositoryResult<String>

    suspend fun saveErikSpellingWords(words: List<SpellingWord>): RepositoryResult<String>
    suspend fun saveMarkSpellingWords(words: List<SpellingWord>): RepositoryResult<String>

    suspend fun modifyErikSpellingWord(wordOld: String, wordNew: String): RepositoryResult<String>
    suspend fun modifyMarkSpellingWord(wordOld: String, wordNew: String): RepositoryResult<String>

    //Spanish
    suspend fun getZitaSpanishWords(enToSp: Boolean?): RepositoryResult<List<SpanishWord>>
    suspend fun getWeekSpanishWords(): RepositoryResult<List<SpanishWord>>

    suspend fun updateZitaSpanishWords(words: List<SpanishWord>): RepositoryResult<String>

    suspend fun saveSpanishWords(words: List<SpanishWord>): RepositoryResult<String>

    //suspend fun restoreSpellingWordsFromLogs(): RepositoryResult<SpellingWord>
}

class NetworkWordRepository(private val wordsAPIService: WordsApiService) :
    WordRepository {

    /** Fetches list of ReadingWords from wordsAPIService */
    override suspend fun getReadingWords(): RepositoryResult<List<ReadingWord>> =
        getReadingWords(SheetAction.READ_READING_WORDS)

    override suspend fun getCEWWords(): RepositoryResult<List<ReadingWord>> =
        getReadingWords(SheetAction.READ_READING_CEW)

    private suspend fun getReadingWords(sheetAction: SheetAction): RepositoryResult<List<ReadingWord>> {
        wordsAPIService.getReadingWords(action = sheetAction.value)
            .onSuccess { response ->
                return if (response.items.isNotEmpty()) {
                    RepositoryResult.Success(data = response.items.map { it.asReadingWord() })
                } else {
                    RepositoryResult.Error(message = response.message)
                }
            }
            .onFailure {
                return RepositoryResult.Error(message = it.message.toString())
            }
        return RepositoryResult.Error(message = "Error")
    }

    override suspend fun getHomophones(): RepositoryResult<List<FillInSentence>> =
        getFillInSentences(sheetAction = SheetAction.READ_HOMOPHONES)

    override suspend fun getIrregularVerbs(): RepositoryResult<List<FillInSentence>> =
        getFillInSentences(sheetAction = SheetAction.READ_IRREGULAR_VERBS)

    private suspend fun getFillInSentences(sheetAction: SheetAction): RepositoryResult<List<FillInSentence>> {
        wordsAPIService.getFillInSentences(action = sheetAction.value)
            .onSuccess { response ->
                return if (response.items.isNotEmpty()) {
                    RepositoryResult.Success(response.items.map { it.asFillInSentence() })
                } else {
                    RepositoryResult.Error(response.message)
                }
            }
            .onFailure {
                Log.e("onFailure", it.message.toString())
                return RepositoryResult.Error(it.message.toString())
            }
        return RepositoryResult.Error("Error")

    }

    override suspend fun getMarkSpellingWords(): RepositoryResult<List<SpellingWord>> =
        getSpellingWords(SheetAction.READ_MARK_SPELLING_WORDS)

    override suspend fun getErikSpellingWords(): RepositoryResult<List<SpellingWord>> =
        getSpellingWords(SheetAction.READ_ERIK_SPELLING_WORDS)

    private suspend fun getSpellingWords(sheetAction: SheetAction): RepositoryResult<List<SpellingWord>> {
        wordsAPIService.getSpellingWords(action = sheetAction.value)
            .onSuccess { response ->
                return if (response.items.isNotEmpty()) {
                    RepositoryResult.Success(data = response.items.map { it.asSpellingWord() })
                } else {
                    RepositoryResult.Error(message = response.message)
                }
            }
            .onFailure {
                Log.e("onFailure", it.message.toString())
                return RepositoryResult.Error(message = it.message.toString())
            }
        return RepositoryResult.Error(message = "Error")

    }

    override suspend fun getErikCategories(): RepositoryResult<List<String>> =
        getCategories(SheetAction.READ_ERIK_SPELLING_CATEGORIES)

    override suspend fun getMarkCategories(): RepositoryResult<List<String>> =
        getCategories(SheetAction.READ_MARK_SPELLING_CATEGORIES)

    private suspend fun getCategories(sheetAction: SheetAction): RepositoryResult<List<String>> {
        wordsAPIService.getCategories(sheetAction.value)
            .onSuccess { response ->
                return if (response.categories.isNotEmpty()) {
                    RepositoryResult.Success(data = response.categories)
                } else {
                    RepositoryResult.Error(response.message)
                }
            }
            .onFailure {
                return RepositoryResult.Error(it.message.toString())
            }
        return RepositoryResult.Error("Error")
    }

    override suspend fun updateHomophones(sentences: List<FillInSentence>): RepositoryResult<String> {
        val sentenceParam = sentences
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asAPISentence() }

        return updateData(SheetAction.UPDATE_HOMOPHONES, sentenceParam)
    }

    override suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): RepositoryResult<String> {
        val sentenceParam = sentences
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asAPISentence() }

        return updateData(SheetAction.UPDATE_IRREGULAR_VERBS, sentenceParam)
    }

    override suspend fun updateErikSpellingWords(words: List<SpellingWord>): RepositoryResult<String> {
        val wordsParam = words
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asAPISellingWord() }

        return updateData(SheetAction.UPDATE_ERIK_SPELLING_WORDS, wordsParam)
    }

    override suspend fun updateMarkSpellingWords(words: List<SpellingWord>): RepositoryResult<String> {
        val wordsParam = words
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asAPISellingWord() }

        return updateData(SheetAction.UPDATE_MARK_SPELLING_WORDS, wordsParam)
    }

    override suspend fun saveErikSpellingWords(words: List<SpellingWord>): RepositoryResult<String> {
        val itemsToSave = words.map { it.asAPISellingWord() }
        return updateData(SheetAction.SAVE_ERIK_WORDS, itemsToSave)
    }

    override suspend fun saveMarkSpellingWords(words: List<SpellingWord>): RepositoryResult<String> {
        val itemsToSave = words.map { it.asAPISellingWord() }
        return updateData(SheetAction.SAVE_MARK_WORDS, itemsToSave)
    }

    override suspend fun modifyErikSpellingWord(
        wordOld: String,
        wordNew: String
    ): RepositoryResult<String> {
        val items = listOf(wordOld, wordNew)
        return updateData(SheetAction.MODIFY_ERIK_SPELLING_WORD, items)
    }

    override suspend fun modifyMarkSpellingWord(
        wordOld: String,
        wordNew: String
    ): RepositoryResult<String> {
        val items = listOf(wordOld, wordNew)
        return updateData(SheetAction.MODIFY_MARK_SPELLING_WORD, items)
    }

    override suspend fun getZitaSpanishWords(enToSp: Boolean?): RepositoryResult<List<SpanishWord>> {
        return getSpanishWords(SheetAction.READ_ZITA_SPANISH_WORDS, enToSp)
    }

    override suspend fun getWeekSpanishWords(): RepositoryResult<List<SpanishWord>> {
        return getSpanishWords(SheetAction.READ_WEEK_SPANISH_WORDS, true)
    }

    override suspend fun updateZitaSpanishWords(words: List<SpanishWord>): RepositoryResult<String> {
        val itemsToUpdate = words.filter { it.status != WordStatus.UNCHECKED }
            .map { it.asApiSpanishWord() }
        return updateData(SheetAction.UPDATE_ZITA_SPANISH_WORDS, itemsToUpdate)
    }

    override suspend fun saveSpanishWords(words: List<SpanishWord>): RepositoryResult<String> {
        val itemsToUpdate = words.map { it.asApiSpanishWord() }
        return updateData(SheetAction.SAVE_ZITA_SPANISH_WORDS, itemsToUpdate)
    }

    private suspend fun getSpanishWords(
        sheetAction: SheetAction,
        enToSp: Boolean?
    ): RepositoryResult<List<SpanishWord>> {
        wordsAPIService.getSpanishWords(action = sheetAction.value)
            .onSuccess { response ->
                return if (response.items.isNotEmpty()) {
                    RepositoryResult.Success(data = response.items.map { it.asSpanishWord(enToSp) })
                } else {
                    RepositoryResult.Error(message = response.message)
                }
            }
            .onFailure {
                Log.e("onFailure", it.message.toString())
                return RepositoryResult.Error(message = it.message.toString())
            }
        return RepositoryResult.Error(message = "Error")
    }

    private suspend inline fun <reified T> updateData(
        sheetAction: SheetAction,
        itemsToUpdate: List<T>
    ): RepositoryResult<String> {
        if (itemsToUpdate.isEmpty()) {
            return RepositoryResult.Error(message = "No data has given")
        }

        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

        val params = PostApiParameter(itemsToUpdate, sheetAction.value)

        wordsAPIService.updateData(
            parameter = json.encodeToJsonElement(params)
        )
            .onSuccess { response ->
                return if (response.result.isNotEmpty()) {
                    RepositoryResult.Success(data = response.result)
                } else {
                    RepositoryResult.Error(message = response.message)
                }
            }
            .onFailure {
                Log.e("onFailure", it.message.toString())
                return RepositoryResult.Error(
                    message = it.message.toString()
                )
            }
        return RepositoryResult.Error(message = "Error")
    }
}