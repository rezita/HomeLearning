package com.github.rezita.homelearning.data

import com.github.rezita.homelearning.network.ResponseCallAdapterFactory
import com.github.rezita.homelearning.network.WordsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val wordRepository: WordRepository
}

class DefaultAppContainer : AppContainer {
    override val wordRepository: WordRepository by lazy {
        NetworkWordRepository(retrofitService)
    }

    val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .readTimeout(60000, TimeUnit.MILLISECONDS)  //1 min
        .writeTimeout(120000, TimeUnit.MILLISECONDS)    //2 min
        .build()

    private val baseUrl =
        "https://script.google.com"

    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(ResponseCallAdapterFactory())
        .baseUrl(baseUrl)
        .client(client)
        .build()

    private val retrofitService: WordsApiService by lazy {
        retrofit.create(WordsApiService::class.java)
    }
}