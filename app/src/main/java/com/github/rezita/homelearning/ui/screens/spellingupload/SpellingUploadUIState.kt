package com.github.rezita.homelearning.ui.screens.spellingupload

import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.screens.spellingupload.edit.EditState
import com.github.rezita.homelearning.ui.viewmodels.MAX_NR_OF_WORDS

sealed interface SpellingUploadUiState {
    fun isExpandable(): Boolean
    fun isSavable(): Boolean

    data object Loading : SpellingUploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false

    }

    data object Saving : SpellingUploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false

    }

    data object NoWords : SpellingUploadUiState {
        override fun isExpandable() = true
        override fun isSavable() = false

    }

    data class HasWords(
        val words: List<SpellingWord> = emptyList(),
    ) : SpellingUploadUiState {
        override fun isExpandable() = words.size < MAX_NR_OF_WORDS
        override fun isSavable() = true

    }

    data class Saved(
        val words: List<SpellingWord>,
        val savingResult: List<Pair<SpellingWord, String>> = emptyList(),
    ) : SpellingUploadUiState {
        override fun isExpandable() = true
        override fun isSavable() = false

    }

    data class Editing(
        val editState: EditState = EditState(),
        val categories: List<String>,
    ) : SpellingUploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false
    }

    data class LoadingError(
        val errorMessage: Int?,
    ) : SpellingUploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false

    }

    data class SavingError(
        val words: List<SpellingWord>,
        val errorMessage: Int?,
    ) : SpellingUploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false
    }
}
