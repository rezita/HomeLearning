package com.github.rezita.homelearning.ui.screens.upload.common

import com.github.rezita.homelearning.model.Uploadable
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditState

sealed interface UploadUiState<out T : Uploadable> {
    fun isExpandable(): Boolean
    fun isSavable(): Boolean
    fun canNavBack(): Boolean

    data object Loading : UploadUiState<Nothing> {
        override fun isExpandable() = false
        override fun isSavable() = false
        override fun canNavBack() = false
    }

    data object Saving : UploadUiState<Nothing> {
        override fun isExpandable() = false
        override fun isSavable() = false
        override fun canNavBack() = false
    }

    data object NoWords : UploadUiState<Nothing> {
        override fun isExpandable() = true
        override fun isSavable() = false
        override fun canNavBack() = true
    }

    data class HasWords<T : Uploadable>(
        val words: List<T> = emptyList(),
    ) : UploadUiState<T> {
        override fun isExpandable() = words.size < MAX_NR_OF_WORDS
        override fun isSavable() = true
        override fun canNavBack() = true
    }

    data class Saved<T : Uploadable>(
        val words: List<T>,
        val savingResult: List<Pair<T, String>> = emptyList(),
    ) : UploadUiState<T> {
        override fun isExpandable() = true
        override fun isSavable() = false
        override fun canNavBack() = true
    }

    data class Editing<T : Uploadable>(
        val editState: EditState<T>,
        val categories: List<String>,
    ) : UploadUiState<T> {
        override fun isExpandable() = false
        override fun isSavable() = false
        override fun canNavBack() = false
    }

    data class SavingError<T : Uploadable>(
        val words: List<T>,
        val errorMessage: Int?,
    ) : UploadUiState<T> {
        override fun isExpandable() = false
        override fun isSavable() = false
        override fun canNavBack() = true
    }

    data class LoadingError<T : Uploadable>(
        val errorMessage: Int?,
    ) : UploadUiState<T> {
        override fun isExpandable() = false
        override fun isSavable() = false
        override fun canNavBack() = true
    }
}