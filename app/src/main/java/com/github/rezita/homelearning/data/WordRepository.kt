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
    suspend fun getReadingWords(): SimpleRepositoryResult<ReadingWord>
    suspend fun getCEWWords(): SimpleRepositoryResult<ReadingWord>

    suspend fun getIrregularVerbs(): NormalRepositoryResult<FillInSentence>
    suspend fun getHomophones(): NormalRepositoryResult<FillInSentence>

    suspend fun getErikSpellingWords(): NormalRepositoryResult<SpellingWord>
    suspend fun getMarkSpellingWords(): NormalRepositoryResult<SpellingWord>

    suspend fun getErikCategories(): ComplexRepositoryResult<String, SpellingWord>
    suspend fun getMarkCategories(): ComplexRepositoryResult<String, SpellingWord>

    suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): NormalRepositoryResult<FillInSentence>
    suspend fun updateHomophones(sentences: List<FillInSentence>): NormalRepositoryResult<FillInSentence>

    suspend fun updateErikSpellingWords(words: List<SpellingWord>): NormalRepositoryResult<SpellingWord>
    suspend fun updateMarkSpellingWords(words: List<SpellingWord>): NormalRepositoryResult<SpellingWord>

    suspend fun saveErikSpellingWords(
        words: List<SpellingWord>,
        downloaded: List<String>
    ): ComplexRepositoryResult<String, SpellingWord>

    suspend fun saveMarkSpellingWords(
        words: List<SpellingWord>,
        downloaded: List<String>
    ): ComplexRepositoryResult<String, SpellingWord>

    //suspend fun restoreSpellingWordsFromLogs(): RepositoryResult<SpellingWord>
}

class NetworkWordRepository(private val wordsAPIService: WordsApiService) :
    WordRepository {

    /** Fetches list of ReadingWords from wordsAPIService */
    override suspend fun getReadingWords(): SimpleRepositoryResult<ReadingWord> =
        getReadingWords(SheetAction.READ_READING_WORDS)

    override suspend fun getCEWWords(): SimpleRepositoryResult<ReadingWord> =
        getReadingWords(SheetAction.READ_READING_CEW)

    private suspend fun getReadingWords(sheetAction: SheetAction): SimpleRepositoryResult<ReadingWord> {
        wordsAPIService.getReadingWords(action = sheetAction.value)
            .onSuccess { response ->
                return if (response.items.isNotEmpty()) {
                    SimpleRepositoryResult.Downloaded(response.items.map { it.asReadingWord() })
                } else {
                    SimpleRepositoryResult.DownloadingError(response.message)
                }
            }
            .onFailure {
                return SimpleRepositoryResult.DownloadingError(it.message.toString())
            }
        return SimpleRepositoryResult.DownloadingError("Error")
    }

    override suspend fun getHomophones(): NormalRepositoryResult<FillInSentence> =
        getFillInSentences(sheetAction = SheetAction.READ_HOMOPHONES)

    override suspend fun getIrregularVerbs(): NormalRepositoryResult<FillInSentence> =
        getFillInSentences(sheetAction = SheetAction.READ_IRREGULAR_VERBS)

    private suspend fun getFillInSentences(sheetAction: SheetAction): NormalRepositoryResult<FillInSentence> {
        wordsAPIService.getFillInSentences(action = sheetAction.value)
            .onSuccess { response ->
                return if (response.items.isNotEmpty()) {
                    NormalRepositoryResult.Downloaded(response.items.map { it.asFillInSentence() })
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

    override suspend fun getErikCategories(): ComplexRepositoryResult<String, SpellingWord> =
        getCategories(SheetAction.READ_ERIK_SPELLING_CATEGORIES)

    override suspend fun getMarkCategories(): ComplexRepositoryResult<String, SpellingWord> =
        getCategories(SheetAction.READ_MARK_SPELLING_CATEGORIES)

    private suspend fun getCategories(sheetAction: SheetAction): ComplexRepositoryResult<String, SpellingWord> {
        wordsAPIService.getCategories(sheetAction.value)
            .onSuccess { response ->
                return if (response.categories.isNotEmpty()) {
                    ComplexRepositoryResult.Downloaded(
                        downloaded = response.categories,
                        uploadable = emptyList()
                    )
                } else {
                    ComplexRepositoryResult.DownloadingError(response.message)
                }
            }
            .onFailure {
                Log.e("onFailure", it.message.toString())
                return ComplexRepositoryResult.DownloadingError(it.message.toString())
            }
        return ComplexRepositoryResult.DownloadingError("Error")
    }

    override suspend fun updateHomophones(sentences: List<FillInSentence>): NormalRepositoryResult<FillInSentence> {
        val result = updateFillInSentences(
            sheetAction = SheetAction.UPDATE_HOMOPHONES,
            sentences = sentences
        )
        Log.i("result01", result.toString())
        return result
    }

    override suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): NormalRepositoryResult<FillInSentence> =
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

    override suspend fun updateMarkSpellingWords(uploadable: List<SpellingWord>): NormalRepositoryResult<SpellingWord> =
        updateSpellingWords(
            sheetAction = SheetAction.UPDATE_MARK_SPELLING_WORDS,
            words = uploadable
        )

    override suspend fun saveErikSpellingWords(
        uploadable: List<SpellingWord>,
        downloaded: List<String>
    ): ComplexRepositoryResult<String, SpellingWord> =
        saveSpellingWords(
            downloaded = downloaded,
            sheetAction = SheetAction.SAVE_ERIK_WORDS,
            words = uploadable
        )

    override suspend fun saveMarkSpellingWords(
        words: List<SpellingWord>,
        downloaded: List<String>
    ): ComplexRepositoryResult<String, SpellingWord> =
        saveSpellingWords(
            downloaded = downloaded,
            sheetAction = SheetAction.SAVE_MARK_WORDS,
            words = words
        )

    private suspend fun saveSpellingWords(
        downloaded: List<String>,
        sheetAction: SheetAction,
        words: List<SpellingWord>
    ): ComplexRepositoryResult<String, SpellingWord> {
        val wordsParam = words.map { it.asAPISellingWord() }

        if (wordsParam.isEmpty()) {
            return ComplexRepositoryResult.UploadError(
                downloaded = downloaded,
                uploadable = words,
                message = "No data has given"
            )
        }
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        val params = PostApiParameter(
            items = wordsParam,
            action = sheetAction.value
        )
        wordsAPIService.updateData(parameter = json.encodeToJsonElement(params))
            .onSuccess { response ->
                return if (response.result.isNotEmpty()) {
                    ComplexRepositoryResult.Uploaded(
                        downloaded = downloaded,
                        uploadable = emptyList(),
                        message = response.result,
                    )
                } else {
                    ComplexRepositoryResult.UploadError(
                        downloaded = downloaded,
                        uploadable = words,
                        message = response.message
                    )
                }
            }
            .onFailure {
                Log.e("onFailure", it.message.toString())
                return ComplexRepositoryResult.UploadError(
                    downloaded = downloaded,
                    uploadable = words,
                    message = it.message.toString()
                )
            }
        return return ComplexRepositoryResult.UploadError(
            downloaded = downloaded,
            uploadable = words,
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
    ): NormalRepositoryResult<FillInSentence> {
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
                    NormalRepositoryResult.Uploaded(data = sentences, message = response.result)
                } else {
                    NormalRepositoryResult.UploadError(data = sentences, message = response.message)
                }
            }
            .onFailure {
                Log.e("onFailure", it.message.toString())
                return NormalRepositoryResult.UploadError(
                    data = sentences,
                    message = it.message.toString()
                )
            }
        return NormalRepositoryResult.UploadError(data = sentences, message = "Error")
    }

    /*
    override suspend fun restoreSpellingWordsFromLogs(): String {
        val sheetAction = SheetAction.RESTORE_ERIK_SPELLING_FROM_LOG.value.toRequestBody()
        return wordsAPIService.restoreSpellingWordsFromLogs(action = sheetAction)
    }
    */
}