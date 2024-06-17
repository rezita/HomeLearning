package com.github.rezita.homelearning.ui.screens.uploadwords

import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.screens.uploadwords.edit.EditState
import com.github.rezita.homelearning.ui.viewmodels.MAX_NR_OF_WORDS

sealed interface UploadUiState {
    fun isExpandable(): Boolean
    fun isSavable(): Boolean

    data object Loading : UploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false

    }

    data object Saving : UploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false

    }

    data object NoWords : UploadUiState {
        override fun isExpandable() = true
        override fun isSavable() = false

    }

    data class HasWords(
        val words: List<SpellingWord> = emptyList(),
    ) : UploadUiState {
        override fun isExpandable() = words.size < MAX_NR_OF_WORDS
        override fun isSavable() = true

    }

    data class Saved(
        val words: List<SpellingWord>,
        val savingResult: List<Pair<SpellingWord, String>> = emptyList(),
    ) : UploadUiState {
        override fun isExpandable() = true
        override fun isSavable() = false

    }

    data class Editing(
        val editState: EditState = EditState(),
        val categories: List<String>,
    ) : UploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false
    }

    data class LoadingError(
        val errorMessage: Int?,
    ) : UploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false

    }

    data class SavingError(
        val words: List<SpellingWord>,
        val errorMessage: Int?,
    ) : UploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false
    }
}
