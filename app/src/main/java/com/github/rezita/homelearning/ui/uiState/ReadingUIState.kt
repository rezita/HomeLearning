package com.github.rezita.homelearning.ui.uiState

import com.github.rezita.homelearning.model.ReadingWord

data class ReadingUIState(
    val words: List<ReadingWord> = emptyList(),
    val message: String = "",
    val state: UIState = UIState.LOADING,
){
    val getInfoMessage: String = when (state) {
        UIState.ERROR -> message
        UIState.LOADING -> "Loading..."
        else -> if (words.isEmpty()) {
            "The are no words"
        } else {
            ""
        }
    }
}