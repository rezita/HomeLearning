package com.github.rezita.homelearning.ui.screens.spelling

import com.github.rezita.homelearning.model.SpellingWord

sealed interface SpellingUiState {
    val isSavable: Boolean

    data class Loading(
        override val isSavable: Boolean
    ) : SpellingUiState

    data class Loaded(
        val words: List<SpellingWord>,
        override val isSavable: Boolean
    ) : SpellingUiState

    data class LoadingError(
        val errorMessage: Int,
        override val isSavable: Boolean
    ) : SpellingUiState

    data class Saved(
        val words: List<SpellingWord>,
        override val isSavable: Boolean
    ) : SpellingUiState

    data class SavingError(
        val errorMessage: Int,
        val words: List<SpellingWord>,
        override val isSavable: Boolean
    ) : SpellingUiState
}
