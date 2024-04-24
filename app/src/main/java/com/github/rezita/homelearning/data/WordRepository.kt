package com.github.rezita.homelearning.data

import android.util.Log
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.PostApiParameter
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.model.asAPISellingWord
import com.github.rezita.homelearning.model.asAPISentence
import com.github.rezita.homelearning.model.asFillInSentence
import com.github.rezita.homelearning.model.asReadingWord
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

    suspend fun getErikSpellingWords(): NormalRepositoryResult<SpellingWord>
    suspend fun getMarkSpellingWords(): NormalRepositoryResult<SpellingWord>

    suspend fun getErikCategories(): RepositoryResult<List<String>>
    suspend fun getMarkCategories(): RepositoryResult<List<String>>

    suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): RepositoryResult<String>
    suspend fun updateHomophones(sentences: List<FillInSentence>): RepositoryResult<String>

    suspend fun updateErikSpellingWords(words: List<SpellingWord>): NormalRepositoryResult<SpellingWord>
    suspend fun updateMarkSpellingWords(words: List<SpellingWord>): NormalRepositoryResult<SpellingWord>

    suspend fun saveErikSpellingWords(words: List<SpellingWord>): RepositoryResult<String>

    suspend fun saveMarkSpellingWords(words: List<SpellingWord>): RepositoryResult<String>

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

    override suspend fun getMarkSpellingWords(): NormalRepositoryResult<SpellingWord> =
        getSpellingWords(SheetAction.READ_MARK_SPELLING_WORDS)

    override suspend fun getErikSpellingWords(): NormalRepositoryResult<SpellingWord> =
        getSpellingWords(SheetAction.READ_ERIK_SPELLING_WORDS)

    private suspend fun getSpellingWords(sheetAction: SheetAction): NormalRepositoryResult<SpellingWord> {
        wordsAPIService.getSpellingWords(action = sheetAction.value)
            .onSuccess { response ->
                return if (response.items.isNotEmpty()) {
                    NormalRepositoryResult.Downloaded(response.items.map { it.asSpellingWord() })
                } else {
                    NormalRepositoryResult.DownloadingError(response.message)
                }
            }
            .onFailure {
                Log.e("onFailure", it.message.toString())
                return NormalRepositoryResult.DownloadingError(it.message.toString())
            }
        return NormalRepositoryResult.DownloadingError("Error")

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
                Log.e("onFailure", it.message.toString())
                return RepositoryResult.Error(it.message.toString())
            }
        return RepositoryResult.Error("Error")
    }

    override suspend fun updateHomophones(sentences: List<FillInSentence>): RepositoryResult<String> {
        val result = updateFillInSentences(
            sheetAction = SheetAction.UPDATE_HOMOPHONES,
            sentences = sentences
        )
        Log.i("result01", result.toString())
        return result
    }

    override suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): RepositoryResult<String> =
        updateFillInSentences(
            sheetAction = SheetAction.UPDATE_IRREGULAR_VERBS,
            sentences = sentences
        )

    override suspend fun updateErikSpellingWords(words: List<SpellingWord>): NormalRepositoryResult<SpellingWord> {
        return updateSpellingWords(
            sheetAction = SheetAction.UPDATE_ERIK_SPELLING_WORDS,
            words = words
        )
    }

    override suspend fun updateMarkSpellingWords(words: List<SpellingWord>): NormalRepositoryResult<SpellingWord> =
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
                Log.e("onFailure", it.message.toString())
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
    ): NormalRepositoryResult<SpellingWord> {
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
                        NormalRepositoryResult.Uploaded(data = words, message = response.result)
                    } else {
                        NormalRepositoryResult.UploadError(data = words, message = response.message)
                    }
                }
                .onFailure {
                    Log.e("onFailure", it.message.toString())
                    return NormalRepositoryResult.UploadError(
                        data = words,
                        message = it.message.toString()
                    )
                }
            return NormalRepositoryResult.UploadError(data = words, message = "Error")
        } else {
            return NormalRepositoryResult.UploadError(data = words, message = "No data has given")
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
}