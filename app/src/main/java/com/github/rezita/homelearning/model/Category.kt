package com.github.rezita.homelearning.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val categories: List<String>
)