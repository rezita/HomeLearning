package com.github.rezita.homelearning.ui.screens.uploadwords

import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.screens.uploadwords.edit.EditState

sealed interface UploadUiState {
    val categories: List<String>
    val isExpandable: Boolean
    val isSavable: Boolean


    data class Loading(
        override val categories: List<String>,
        override val isExpandable: Boolean,
        override val isSavable: Boolean
    ) : UploadUiState

    data class Saving(
        val words: List<SpellingWord>,
        override val categories: List<String>,
        override val isExpandable: Boolean,
        override val isSavable: Boolean
    ) : UploadUiState

    data class NoWords(
        override val categories: List<String>,
        override val isExpandable: Boolean,
        override val isSavable: Boolean
    ) : UploadUiState

    data class HasWords(
        val words: List<SpellingWord> = emptyList(),
        override val categories: List<String>,
        override val isExpandable: Boolean,
        override val isSavable: Boolean
    ) : UploadUiState

    data class Saved(
        val words: List<SpellingWord>,
        val savingResult: List<Pair<SpellingWord, String>> = emptyList(),
        override val isExpandable: Boolean,
        override val categories: List<String>,
        override val isSavable: Boolean
    ) : UploadUiState

    data class Editing(
        val editState: EditState = EditState(),
        override val isExpandable: Boolean,
        override val categories: List<String>,
        override val isSavable: Boolean
    ) : UploadUiState

    data class LoadingError(
        val errorMessage: Int?,
        override val isExpandable: Boolean,
        override val categories: List<String>,
        override val isSavable: Boolean
    ) : UploadUiState

    data class SavingError(
        val words: List<SpellingWord>,
        val errorMessage: Int?,
        override val isExpandable: Boolean,
        override val categories: List<String>,
        override val isSavable: Boolean
    ) : UploadUiState
}
