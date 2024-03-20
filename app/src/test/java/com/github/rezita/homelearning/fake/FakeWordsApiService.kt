package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.model.ApiFillInSentence
import com.github.rezita.homelearning.model.ApiReadingWord
import com.github.rezita.homelearning.model.ApiSpellingWord
import com.github.rezita.homelearning.model.Category
import com.github.rezita.homelearning.model.GetRequestApiItems
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.WordsApiService
import kotlinx.serialization.json.JsonElement

class FakeWordsApiService : WordsApiService {
    override suspend fun getReadingWords(action: String): GetRequestApiItems<ApiReadingWord> {
        return FakeReadingDataSource.apiReadingWords
    }

    override suspend fun getFillInSentences(action: String): GetRequestApiItems<ApiFillInSentence> {
        return FakeSentenceDataSource.apiSentences
    }

    override suspend fun getSpellingWords(action: String): GetRequestApiItems<ApiSpellingWord> {
        return FakeSpellingDataSource.apiSpellingWords
    }

    override suspend fun getCategories(action: String): Category {
        return FakeCategoryDataSource.apiCategories
    }

    override suspend fun updateData(parameter: JsonElement): String {
        TODO("Not yet implemented")
    }
}