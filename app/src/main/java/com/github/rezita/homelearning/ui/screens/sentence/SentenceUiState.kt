package com.github.rezita.homelearning.ui.screens.sentence

import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus

sealed interface SentenceUiState {
    //val isSavable: Boolean
    fun isSavable(): Boolean

    //after kotlin v1.9 this can be data object instead of object
    data object Loading : SentenceUiState {
        override fun isSavable() = false
    }

    data class Loaded(val sentences: List<FillInSentence>) : SentenceUiState {
        override fun isSavable() =
            sentences.isNotEmpty() && sentences.none { word -> word.status == WordStatus.UNCHECKED }
    }

    data class LoadingError(val errorMessage: Int) : SentenceUiState {
        override fun isSavable() = false
    }

    data class Saved(val sentences: List<FillInSentence>) : SentenceUiState {
        override fun isSavable() = false
    }

    data class SavingError(
        val errorMessage: Int,
        val sentences: List<FillInSentence>,
    ) : SentenceUiState {
        override fun isSavable() = true
    }
}
