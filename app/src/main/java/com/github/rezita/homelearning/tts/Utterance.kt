package com.github.rezita.homelearning.tts

data class Utterance(
    val id: String,
    val text: String,
    val queueMode: Int
)
