package com.github.rezita.homelearning.data

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
    suspend fun getReadingWords(): List<ReadingWord>
    suspend fun getCEWWords(): List<ReadingWord>

    suspend fun getIrregularVerbs(): List<FillInSentence>
    suspend fun getHomophones(): List<FillInSentence>

    suspend fun getErikSpellingWords(): List<SpellingWord>
    suspend fun getMarkSpellingWords(): List<SpellingWord>

    suspend fun getErikCategories(): List<String>
    suspend fun getMarkCategories(): List<String>

    suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): String
    suspend fun updateHomophones(sentences: List<FillInSentence>): String

    suspend fun updateErikSpellingWords(words: List<SpellingWord>): String
    suspend fun updateMarkSpellingWords(words: List<SpellingWord>): String

    suspend fun saveErikSpellingWords(words: List<SpellingWord>): String
    suspend fun saveMarkSpellingWords(words: List<SpellingWord>): String

    //suspend fun restoreSpellingWordsFromLogs(): String
}

class NetworkWordRepository(private val wordsAPIService: WordsApiService) :
    WordRepository {

    /** Fetches list of ReadingWords from wordsAPIService */
    override suspend fun getReadingWords(): List<ReadingWord> =
        getReadingWords(SheetAction.READ_READING_WORDS)

    override suspend fun getCEWWords(): List<ReadingWord> =
        getReadingWords(SheetAction.READ_READING_CEW)

    private suspend fun getReadingWords(sheetAction: SheetAction): List<ReadingWord> =
        wordsAPIService.getReadingWords(sheetAction.value).items.map { it.asReadingWord() }

    override suspend fun getHomophones(): List<FillInSentence> =
        getFillInSentences(sheetAction = SheetAction.READ_HOMOPHONES)

    override suspend fun getIrregularVerbs(): List<FillInSentence> =
        getFillInSentences(sheetAction = SheetAction.READ_IRREGULAR_VERBS)

    private suspend fun getFillInSentences(sheetAction: SheetAction): List<FillInSentence> =
        wordsAPIService.getFillInSentences(sheetAction.value).items.map { it.asFillInSentence() }

    override suspend fun getMarkSpellingWords(): List<SpellingWord> =
        getSpellingWords(SheetAction.READ_MARK_SPELLING_WORDS)

    override suspend fun getErikSpellingWords(): List<SpellingWord> =
        getSpellingWords(SheetAction.READ_ERIK_SPELLING_WORDS)

    private suspend fun getSpellingWords(sheetAction: SheetAction): List<SpellingWord> =
        wordsAPIService.getSpellingWords(sheetAction.value).items.map { it.asSpellingWord() }

    override suspend fun getErikCategories(): List<String> =
        getCategories(SheetAction.READ_ERIK_SPELLING_CATEGORIES)

    override suspend fun getMarkCategories(): List<String> =
        getCategories(SheetAction.READ_MARK_SPELLING_CATEGORIES)

    private suspend fun getCategories(sheetAction: SheetAction): List<String> =
        wordsAPIService.getCategories(sheetAction.value).categories

    override suspend fun updateHomophones(sentences: List<FillInSentence>): String =
        updateFillInSentences(sheetAction = SheetAction.UPDATE_HOMOPHONES, sentences = sentences)

    override suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): String =
        updateFillInSentences(
            sheetAction = SheetAction.UPDATE_IRREGULAR_VERBS,
            sentences = sentences
        )

    override suspend fun updateErikSpellingWords(words: List<SpellingWord>): String =
        updateSpellingWords(SheetAction.UPDATE_ERIK_SPELLING_WORDS, words)

    override suspend fun updateMarkSpellingWords(words: List<SpellingWord>): String =
        updateSpellingWords(SheetAction.UPDATE_MARK_SPELLING_WORDS, words)


    override suspend fun saveErikSpellingWords(words: List<SpellingWord>): String =
        saveSpellingWords(SheetAction.SAVE_ERIK_WORDS, words)

    override suspend fun saveMarkSpellingWords(words: List<SpellingWord>): String =
        saveSpellingWords(SheetAction.SAVE_MARK_WORDS, words)

    private suspend fun saveSpellingWords(
        sheetAction: SheetAction,
        words: List<SpellingWord>
    ): String {
        val wordsParam = words.map { it.asAPISellingWord() }

        return if (wordsParam.isNotEmpty()) {
            val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
            val params = PostApiParameter(
                items = wordsParam,
                action = sheetAction.value
            )
            wordsAPIService.updateData(
                parameter = json.encodeToJsonElement(params)
            )
        } else {
            ""
        }
    }

    private suspend fun updateSpellingWords(
        sheetAction: SheetAction,
        words: List<SpellingWord>
    ): String {
        val wordsParam = words
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asAPISellingWord() }

        return if (wordsParam.isNotEmpty()) {
            val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
            val params = PostApiParameter(
                items = wordsParam,
                action = sheetAction.value
            )
            return wordsAPIService.updateData(
                parameter = json.encodeToJsonElement(params)
            )
            //callingUpdateData(wordsParam, sheetAction)
        } else {
            ""
        }
    }

    private suspend fun updateFillInSentences(
        sheetAction: SheetAction,
        sentences: List<FillInSentence>
    ): String {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

        val sentenceParam = sentences
            .filter { it.status != WordStatus.UNCHECKED }
            .map { it.asAPISentence() }

        val params = PostApiParameter(
            items = sentenceParam,
            action = sheetAction.value
        )
        return wordsAPIService.updateData(
            parameter = json.encodeToJsonElement(params)
        )
    }
    /*
        private suspend fun <T> callingUpdateData(words: List<T>, action: SheetAction): String {
            val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

            Log.i("words", words.toString())
            Log.i("action", action.toString())


            val params = PostApiParameters(
                words = words,
                action = action.value
            )
            return wordsAPIService.updateData(
                parameter = json.encodeToJsonElement(PostParameters(params))
            )
        }

     */

    /*
    override suspend fun restoreSpellingWordsFromLogs(): String {
        val sheetAction = SheetAction.RESTORE_ERIK_SPELLING_FROM_LOG.value.toRequestBody()
        return wordsAPIService.restoreSpellingWordsFromLogs(action = sheetAction)
    }
*/
}