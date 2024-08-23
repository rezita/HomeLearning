package com.github.rezita.homelearning.ui.screens.spelling

import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.viewmodels.SpellingEditState

sealed interface SpellingUiState {
    /** for displaying the action icon on the TopAppBar */
    fun isSavable(): Boolean

    /** from Kotlin 1.9 the cna be data object instead of object */
    data object Loading : SpellingUiState {
        override fun isSavable() = false
    }

    data class Loaded(
        val words: List<SpellingWord>,
    ) : SpellingUiState {
        override fun isSavable() =
            words.isNotEmpty() && words.any { word -> word.status != WordStatus.UNCHECKED }
    }

    data class LoadingError(
        val errorMessage: Int,
    ) : SpellingUiState {
        override fun isSavable() = false
    }

    data class Saved(
        val words: List<SpellingWord>
    ) : SpellingUiState {
        override fun isSavable() = false
    }

    data class SavingError(
        val errorMessage: Int,
        val words: List<SpellingWord>,
    ) : SpellingUiState {
        override fun isSavable() = false
    }

    data class Editing(
        val words: List<SpellingWord>,
        val editState: SpellingEditState,
        val isEditing: Boolean
    ): SpellingUiState{
        override fun isSavable() = false
    }

    data class EditError(
        val errorMessage: String,
        val words: List<SpellingWord>,
        val editState: SpellingEditState
    ): SpellingUiState{
        override fun isSavable() = false
    }
}
