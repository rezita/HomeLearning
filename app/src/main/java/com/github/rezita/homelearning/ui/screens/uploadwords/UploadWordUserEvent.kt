package com.github.rezita.homelearning.ui.screens.uploadwords

interface UploadWordUserEvent {
    data object OnSave : UploadWordUserEvent
    data object OnLoad : UploadWordUserEvent
    data object OnAddNew : UploadWordUserEvent
    data class OnRemoveWord(val index: Int) : UploadWordUserEvent
    data class OnPrepareForEditing(val index: Int) : UploadWordUserEvent
    data object OnSaveEditedWord : UploadWordUserEvent
    data object OnCancelEditing : UploadWordUserEvent
    data class OnEditedWordWordChange(val word: String) : UploadWordUserEvent
    data class OnEditedWordCategoryChange(val category: String) : UploadWordUserEvent
    data class OnEditedWordCommentChange(val comment: String) : UploadWordUserEvent

}