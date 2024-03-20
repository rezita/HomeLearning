package com.github.rezita.homelearning.model

import com.github.rezita.homelearning.BuildConfig
import kotlinx.serialization.Serializable

@Serializable
data class GetRequestApiItems<T>(
    val items: List<T>
)

@Serializable
data class PostApiParameter<T>(
    val items: List<T>,
    val action: String,
    val ssId: String = BuildConfig.sheetID
)