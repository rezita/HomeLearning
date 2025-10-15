package com.github.rezita.homelearning.ui.screens.spelling

import com.github.rezita.homelearning.model.WordStatus

sealed interface SpellingUserEvent {
    data object OnDiscardEditing : SpellingUserEvent
    data object OnLoad : SpellingUserEvent
    data object OnSave : SpellingUserEvent
    data object OnSaveEditing : SpellingUserEvent
    data class OnEditItemChange(val value: String) : SpellingUserEvent
    data class OnItemStatusChange(val index: Int, val value: WordStatus) : SpellingUserEvent
    data class OnPrepareForEditing(val index: Int) : SpellingUserEvent
    data class OnSpeakerClicked(val value: String) : SpellingUserEvent
    data class OnValueReset(val index: Int) : SpellingUserEvent
}