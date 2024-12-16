package com.github.rezita.homelearning.ui.screens.sentence

interface SentenceUserEvent {
    data object OnLoad : SentenceUserEvent
    data object OnSave : SentenceUserEvent
    data class OnValueChange(val index: Int, val value: String) : SentenceUserEvent
}