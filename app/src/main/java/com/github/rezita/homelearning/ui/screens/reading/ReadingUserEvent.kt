package com.github.rezita.homelearning.ui.screens.reading

sealed interface ReadingUserEvent {
    
    data object OnLoad: ReadingUserEvent
    data class OnChangeColorDisplay(val visible: Boolean):ReadingUserEvent
}