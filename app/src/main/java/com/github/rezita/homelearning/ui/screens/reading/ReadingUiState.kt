package com.github.rezita.homelearning.ui.screens.reading

import com.github.rezita.homelearning.model.ReadingWord

sealed interface ReadingUiState {

    data object Loading : ReadingUiState

    data class LoadingError(
        val errorMessage: Int,
    ) : ReadingUiState

    data class Downloaded(
        val words: List<ReadingWord>
    ) : ReadingUiState
}