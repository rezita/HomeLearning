package com.github.rezita.homelearning.ui.screens.sentence

import com.github.rezita.homelearning.model.FillInSentence

sealed interface SentenceUiState {
    val isSavable: Boolean

    data class Loading(
        override val isSavable: Boolean
    ) : SentenceUiState

    data class Loaded(
        val sentences: List<FillInSentence>,
        override val isSavable: Boolean
    ) : SentenceUiState

    data class LoadingError(
        val errorMessage: Int,
        override val isSavable: Boolean
    ) : SentenceUiState

    data class Saved(
        val sentences: List<FillInSentence>,
        override val isSavable: Boolean
    ) : SentenceUiState

    data class SavingError(
        val errorMessage: Int,
        val sentences: List<FillInSentence>,
        override val isSavable: Boolean
    ) : SentenceUiState
}
