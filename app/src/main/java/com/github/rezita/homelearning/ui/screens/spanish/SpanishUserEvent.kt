package com.github.rezita.homelearning.ui.screens.spanish

interface SpanishUserEvent {
    data object OnLoad : SpanishUserEvent
    data object OnSave : SpanishUserEvent
    data class OnValueChange(val index: Int, val value: String) : SpanishUserEvent
    data class OnSpeakerClicked(val word: String): SpanishUserEvent
}