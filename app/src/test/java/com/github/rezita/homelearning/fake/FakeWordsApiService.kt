package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.model.ApiFillInSentence
import com.github.rezita.homelearning.model.ApiReadingWord
import com.github.rezita.homelearning.model.ApiSpellingWord
import com.github.rezita.homelearning.model.Category
import com.github.rezita.homelearning.model.GetRequestApiItems
import com.github.rezita.homelearning.model.PostResponse
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.WordsApiService
import kotlinx.serialization.json.JsonElement
import retrofit2.Response

class FakeWordsApiService : WordsApiService {
    override suspend fun getReadingWords(action: String): Result<GetRequestApiItems<ApiReadingWord>> {
        return Result.success(FakeReadingDataSource.apiReadingWords)

    }

    override suspend fun getFillInSentences(action: String): Result<GetRequestApiItems<ApiFillInSentence>> {
        return Result.success(FakeSentenceDataSource.apiSentences)
    }

    override suspend fun getSpellingWords(action: String): Result<GetRequestApiItems<ApiSpellingWord>> {
        return Result.success(FakeSpellingDataSource.apiSpellingWords)
    }

    override suspend fun getCategories(action: String): Result<Category> {
        return Result.success(FakeCategoryDataSource.apiCategories)
    }

    override suspend fun updateData(parameter: JsonElement): Result<PostResponse> {
        TODO("Not yet implemented")
    }
}