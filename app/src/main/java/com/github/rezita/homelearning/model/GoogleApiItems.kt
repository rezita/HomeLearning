package com.github.rezita.homelearning.model

import com.github.rezita.homelearning.BuildConfig
import kotlinx.serialization.Serializable

@Serializable
data class GetRequestApiItems<T>(
    val items: List<T>,
    val message: String
)

@Serializable
data class PostResponse(
    val result: String = "",
    val message: String = ""
)

@Serializable
data class PostApiParameter<T>(
    val items: List<T>,
    val action: String,
    val ssId: String = BuildConfig.sheetID
)

@Serializable
data class Category(
    val categories: List<String>,
    val message: String = ""
)