package com.github.rezita.homelearning.ui.screens.spellingupload

interface SpellingUploadUserEvent {
    data object OnSave : SpellingUploadUserEvent
    data object OnLoad : SpellingUploadUserEvent
    data object OnAddNew : SpellingUploadUserEvent
    data class OnRemoveSpelling(val index: Int) : SpellingUploadUserEvent
    data class OnPrepareForEditing(val index: Int) : SpellingUploadUserEvent
    data object OnSaveEditedSpelling : SpellingUploadUserEvent
    data object OnCancelEditing : SpellingUploadUserEvent
    data class OnEditedWordChangeSpelling(val word: String) : SpellingUploadUserEvent
    data class OnEditedCategoryChangeSpelling(val category: String) : SpellingUploadUserEvent
    data class OnEditedCommentChangeSpelling(val comment: String) : SpellingUploadUserEvent

}