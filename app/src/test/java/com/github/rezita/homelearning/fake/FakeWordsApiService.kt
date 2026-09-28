package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.model.ApiFillInSentence
import com.github.rezita.homelearning.model.ApiReadingWord
import com.github.rezita.homelearning.model.ApiSpanishWord
import com.github.rezita.homelearning.model.ApiSpellingWord
import com.github.rezita.homelearning.model.Category
import com.github.rezita.homelearning.model.GetRequestApiItems
import com.github.rezita.homelearning.model.PostResponse
import com.github.rezita.homelearning.network.WordsApiService
import kotlinx.serialization.json.JsonElement

class FakeWordsApiService : WordsApiService {
    override suspend fun getReadingWords(
        scriptId: String,
        sheetId: String,
        action: String
    ): Result<GetRequestApiItems<ApiReadingWord>> {
        return Result.success(FakeReadingDataSource.apiReadingWords)

    }

    override suspend fun getFillInSentences(
        scriptId: String,
        sheetId: String,
        action: String
    ): Result<GetRequestApiItems<ApiFillInSentence>> {
        return Result.success(FakeSentenceDataSource.apiSentences)
    }

    override suspend fun getSpellingWords(
        scriptId: String,
        sheetId: String,
        action: String
    ): Result<GetRequestApiItems<ApiSpellingWord>> {
        return Result.success(FakeSpellingDataSource.apiSpellingWords)
    }

    override suspend fun getCategories(
        scriptId: String,
        sheetId: String,
        action: String
    ): Result<Category> {
        return Result.success(FakeCategoryDataSource.apiCategories)
    }

    override suspend fun updateData(
        scriptId: String,
        parameter: JsonElement
    ): Result<PostResponse> {
        return Result.success(PostResponse(result = "Success", message = ""))
    }

    override suspend fun getSpanishWords(
        scriptId: String,
        sheetId: String,
        action: String
    ): Result<GetRequestApiItems<ApiSpanishWord>> {
        return Result.success(FakeSpanishDataSource.apiSpanishWords)
    }
}