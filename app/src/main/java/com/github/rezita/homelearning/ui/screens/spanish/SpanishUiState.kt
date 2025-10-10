package com.github.rezita.homelearning.ui.screens.spanish

import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.model.WordStatus

interface SpanishUiState {
    /** for the action icon on the topAppBar */
    fun isSavable(): Boolean

    /** from Kotlin 1.9 the cna be data object instead of object */
    data object Loading : SpanishUiState {
        override fun isSavable() = false
    }

    data class Loaded(val words: List<SpanishWord>) : SpanishUiState {
        override fun isSavable() =
            words.isNotEmpty() && words.none { word -> word.status == WordStatus.UNCHECKED }
    }

    data class LoadingError(val errorMessage: Int) : SpanishUiState {
        override fun isSavable() = false
    }

    data class Saved(val words: List<SpanishWord>) : SpanishUiState {
        override fun isSavable() = false
    }

    data class SavingError(
        val errorMessage: Int,
        val words: List<SpanishWord>,
    ) : SpanishUiState {
        override fun isSavable() = false
    }
}