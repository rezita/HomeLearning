package com.github.rezita.homelearning.ui.uiState

import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus

data class FillInSentenceUIState(
    val sentences: List<FillInSentence> = emptyList(),
    val errorMessage: String? = null,
    //val isLoading: Boolean = false,
    //val isChecked: Boolean = false,
    val state: UIState = UIState.LOADING
) {
    val nrOfQuestions: Int = sentences.size

    val nrOfCorrect: Int = sentences.filter { it.status == WordStatus.CORRECT }.size

    val ratio: Int = if (nrOfQuestions == 0) 0 else nrOfCorrect * 100 / nrOfQuestions

    val isAllAnswered: Boolean = sentences.none { it.status == WordStatus.UNCHECKED }

    val checkable: Boolean = (state == UIState.SUCCESS)
}