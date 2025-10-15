package com.github.rezita.homelearning.ui.screens.upload.common

sealed interface UploadUserEvent : SpellingUploadUserEvent, SpanishUploadUserEvent {
    data object OnSave : UploadUserEvent
    data object OnAddNew : UploadUserEvent
    data class OnRemoveWord(val index: Int) : UploadUserEvent
    data class OnPrepareForEditing(val index: Int) : UploadUserEvent
    data object OnSaveEditedWord : UploadUserEvent
    data object OnCancelEditing : UploadUserEvent

}

sealed interface SpellingUploadUserEvent {
    data object OnLoad : SpellingUploadUserEvent
    data class OnWordChangeForEditedWord(val word: String) : SpellingUploadUserEvent
    data class OnCategoryChangeForEditedWord(val category: String) : SpellingUploadUserEvent
    data class OnCommentChangeForEditedWord(val comment: String) : SpellingUploadUserEvent
}

sealed interface SpanishUploadUserEvent {
    data class OnWordEnChangeForEditedWord(val wordEn: String) : SpanishUploadUserEvent
    data class OnWordSpChangeForEditedWord(val wordSp: String) : SpanishUploadUserEvent
    data class OnCommentChangeForEditedWord(val comment: String) : SpanishUploadUserEvent
}