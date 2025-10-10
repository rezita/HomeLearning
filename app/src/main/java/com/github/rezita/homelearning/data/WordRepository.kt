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
        val result = updateFillInSentences(
            sheetAction = SheetAction.UPDATE_HOMOPHONES,
            sentences = sentences
        )
        return result
    }

    override suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): RepositoryResult<String> =
        updateFillInSentences(
            sheetAction = SheetAction.UPDATE_IRREGULAR_VERBS,
            sentences = sentences
        )

    override suspend fun updateErikSpellingWords(words: List<SpellingWord>): RepositoryResult<String> {
        return updateSpellingWords(
            sheetAction = SheetAction.UPDATE_ERIK_SPELLING_WORDS,
            words = words
        )
    }

    override suspend fun updateMarkSpellingWords(words: List<SpellingWord>): RepositoryResult<String> =
        updateSpellingWords(
            sheetAction = SheetAction.UPDATE_MARK_SPELLING_WORDS,
            words = words
        )

    override suspend fun saveErikSpellingWords(words: List<SpellingWord>): RepositoryResult<String> =
        saveSpellingWords(
            sheetAction = SheetAction.SAVE_ERIK_WORDS,
            words = words
        )

    override suspend fun saveMarkSpellingWords(words: List<SpellingWord>): RepositoryResult<String> =
        saveSpellingWords(
            sheetAction = SheetAction.SAVE_MARK_WORDS,
            words = words
        )

    override suspend fun modifyErikSpellingWord(
        wordOld: String,
        wordNew: String
    ): RepositoryResult<String> =
        modifySpellingWord(
            sheetAction = SheetAction.MODIFY_ERIK_SPELLING_WORD,
            wordOld = wordOld,
            wordNew = wordNew
        )


    override suspend fun modifyMarkSpellingWord(
        wordOld: String,
        wordNew: String
    ): RepositoryResult<String> = modifySpellingWord(
        sheetAction = SheetAction.MODIFY_MARK_SPELLING_WORD,
        wordOld = wordOld,
        wordNew = wordNew
    )

    override suspend fun getZitaSpanishWords(enToSp: Boolean?): RepositoryResult<List<SpanishWord>> {
        return getSpanishWords(SheetAction.READ_ZITA_SPANISH_WORDS, enToSp)
    }

    override suspend fun getWeekSpanishWords(): RepositoryResult<List<SpanishWord>> {
        return getSpanishWords(SheetAction.READ_WEEK_SPANISH_WORDS, true)
    }

    override suspend fun updateZitaSpanishWords(words: List<SpanishWord>): RepositoryResult<String> {
        return updateSpanishWords(
            sheetAction = SheetAction.UPDATE_ZITA_SPANISH_WORDS,
            words = words
        )
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

    private suspend fun saveSpellingWords(
        sheetAction: SheetAction,
        words: List<SpellingWord>
    ): RepositoryResult<String> {
        val wordsParam = words.map { it.asAPISellingWord() }

        if (wordsParam.isEmpty()) {
            return RepositoryResult.Error(message = "No data has given")
        }
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        val params = PostApiParameter(
            items = wordsParam,
            action = sheetAction.value
        )
        wordsAPIService.updateData(parameter = json.encodeToJsonElement(params))
            .onSuccess { response ->
                return if (response.result.isNotEmpty()) {
                    RepositoryResult.Success(
                        data = response.result
                    )
                } else {
                    RepositoryResult.Error(
                        message = response.message
                    )
                }
            }
            .onFailure {
                return RepositoryResult.Error(
                    message = it.message.toString()
                )
            }
        return return RepositoryResult.Error(
            message = "Error"
        )
    }

    private suspend fun updateSpellingWords(
        sheetAction: SheetAction,
        words: List<SpellingWord>
    ): RepositoryResult<String> {
        val wordsParam = words
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asAPISellingWord() }

        if (wordsParam.isNotEmpty()) {
            val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
            val params = PostApiParameter(
                items = wordsParam,
                action = sheetAction.value
            )
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
        } else {
            return RepositoryResult.Error(message = "No data has given")
        }
    }

    private suspend fun updateFillInSentences(
        sheetAction: SheetAction,
        sentences: List<FillInSentence>
    ): RepositoryResult<String> {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

        val sentenceParam = sentences
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asAPISentence() }

        val params = PostApiParameter(
            items = sentenceParam,
            action = sheetAction.value
        )
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

    private suspend fun updateSpanishWords(
        sheetAction: SheetAction,
        words: List<SpanishWord>
    ): RepositoryResult<String> {
        val wordsParam = words
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asApiSpanishWord() }

        if (wordsParam.isNotEmpty()) {
            val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
            val params = PostApiParameter(
                items = wordsParam,
                action = sheetAction.value
            )
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
        } else {
            return RepositoryResult.Error(message = "No data has given")
        }
    }


    private suspend fun modifySpellingWord(
        sheetAction: SheetAction,
        wordOld: String,
        wordNew: String
    ): RepositoryResult<String> {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        val params = PostApiParameter(
            action = sheetAction.value,
            items = listOf(wordOld, wordNew)
        )
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