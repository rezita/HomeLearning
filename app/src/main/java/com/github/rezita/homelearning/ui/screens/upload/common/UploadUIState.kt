package com.github.rezita.homelearning.ui.screens.spellingupload

import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.screens.common.upload.edit.EditState
import com.github.rezita.homelearning.ui.viewmodels.MAX_NR_OF_WORDS

sealed interface UploadUiState : SpellingUploadUiState, SpanishUploadUiState {
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

    data class LoadingError(
        val errorMessage: Int?,
    ) : UploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false
    }
}

interface XXUiState {
    fun isExpandable(): Boolean
    fun isSavable(): Boolean
}

sealed interface SpellingUploadUiState : XXUiState {
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
        val editState: EditState<SpellingWord>,
        val categories: List<String>,
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

sealed interface SpanishUploadUiState : XXUiState {
    data class HasWords(
        val words: List<SpanishWord> = emptyList(),
    ) : SpanishUploadUiState {
        override fun isExpandable() = words.size < MAX_NR_OF_WORDS
        override fun isSavable() = true
    }

    data class Saved(
        val words: List<SpanishWord>,
        val savingResult: List<Pair<SpanishWord, String>> = emptyList(),
    ) : SpanishUploadUiState {
        override fun isExpandable() = true
        override fun isSavable() = false
    }

    data class Editing(
        val editState: EditState<SpanishWord>,
        val categories: List<String>,
    ) : SpanishUploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false
    }

    data class SavingError(
        val words: List<SpanishWord>,
        val errorMessage: Int?,
    ) : SpanishUploadUiState {
        override fun isExpandable() = false
        override fun isSavable() = false
    }
}
