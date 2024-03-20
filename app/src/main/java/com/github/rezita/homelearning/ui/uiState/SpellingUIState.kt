package com.github.rezita.homelearning.ui.uiState

import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus

data class SpellingUIState(
    val words: List<SpellingWord> = emptyList(),
    val message: String? = null,
    val state: UIState = UIState.LOADING
) {
    val answered: Int
        get() = words.filter { it.status != WordStatus.UNCHECKED }.size

    val correct: Int
        get() = words.filter { it.status == WordStatus.CORRECT }.size

    val ratio: Int
        get() = if (answered == 0) 0 else correct * 100 / answered
}
