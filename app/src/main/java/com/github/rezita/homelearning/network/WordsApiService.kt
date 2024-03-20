package com.github.rezita.homelearning.network

import com.github.rezita.homelearning.BuildConfig
import com.github.rezita.homelearning.model.ApiFillInSentence
import com.github.rezita.homelearning.model.ApiReadingWord
import com.github.rezita.homelearning.model.ApiSpellingWord
import com.github.rezita.homelearning.model.Category
import com.github.rezita.homelearning.model.GetRequestApiItems
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

private const val SCRIPT_ID = BuildConfig.scriptID
private const val SHEET_ID = BuildConfig.sheetID

private const val GET_PARAM_URL = "/macros/s/$SCRIPT_ID/exec?ssId=$SHEET_ID"
private const val POST_PARAM_URL = "/macros/s/$SCRIPT_ID/exec"


interface WordsApiService {
    @GET(GET_PARAM_URL)
    suspend fun getReadingWords(@Query("action") action: String): GetRequestApiItems<ApiReadingWord>

    @GET(GET_PARAM_URL)
    suspend fun getFillInSentences(@Query("action") action: String): GetRequestApiItems<ApiFillInSentence>

    @GET(GET_PARAM_URL)
    suspend fun getSpellingWords(@Query("action") action: String): GetRequestApiItems<ApiSpellingWord>

    @GET(GET_PARAM_URL)
    suspend fun getCategories(@Query("action") action: String): Category

    @Headers("Content-Type: application/json")
    @POST(POST_PARAM_URL)
    suspend fun updateData(@Body parameter: JsonElement): String

    /*
        @Headers("Content-Type: application/json")
        @POST(POST_PARAM_URL)
        suspend fun restoreSpellingWordsFromLogs(@Body parameter: JsonElement): String
        */
}