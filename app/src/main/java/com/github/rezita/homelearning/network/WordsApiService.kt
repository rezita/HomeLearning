package com.github.rezita.homelearning.network

import com.github.rezita.homelearning.model.ApiFillInSentence
import com.github.rezita.homelearning.model.ApiReadingWord
import com.github.rezita.homelearning.model.ApiSpanishWord
import com.github.rezita.homelearning.model.ApiSpellingWord
import com.github.rezita.homelearning.model.Category
import com.github.rezita.homelearning.model.GetRequestApiItems
import com.github.rezita.homelearning.model.PostResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val PARAM_URL = "/macros/s/{scriptId}/exec"

interface WordsApiService {
    @GET(PARAM_URL)
    suspend fun getReadingWords(
        @Path("scriptId") scriptId: String,
        @Query("ssId") sheetId: String,
        @Query("action") action: String
    ): Result<GetRequestApiItems<ApiReadingWord>>

    @GET(PARAM_URL)
    suspend fun getFillInSentences(
        @Path("scriptId") scriptId: String,
        @Query("ssId") sheetId: String,
        @Query("action") action: String
    ): Result<GetRequestApiItems<ApiFillInSentence>>

    @GET(PARAM_URL)
    suspend fun getSpellingWords(
        @Path("scriptId") scriptId: String,
        @Query("ssId") sheetId: String,
        @Query("action") action: String
    ): Result<GetRequestApiItems<ApiSpellingWord>>

    @GET(PARAM_URL)
    suspend fun getCategories(
        @Path("scriptId") scriptId: String,
        @Query("ssId") sheetId: String,
        @Query("action") action: String
    ): Result<Category>

    @Headers("Content-Type: application/json")
    @POST(PARAM_URL)
    suspend fun updateData(
        @Path("scriptId") scriptId: String,
        @Body parameter: JsonElement
    ): Result<PostResponse>

    @GET(PARAM_URL)
    suspend fun getSpanishWords(
        @Path("scriptId") scriptId: String,
        @Query("ssId") sheetId: String,
        @Query("action") action: String
    ): Result<GetRequestApiItems<ApiSpanishWord>>

    /*
        @Headers("Content-Type: application/json")
        @POST(POST_PARAM_URL)
        suspend fun restoreSpellingWordsFromLogs(@Body parameter: JsonElement): String
        */
}