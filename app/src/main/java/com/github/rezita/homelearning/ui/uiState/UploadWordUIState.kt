package com.github.rezita.homelearning.ui.uiState

import com.github.rezita.homelearning.model.SpellingWord

const val MAX_NR_OF_WORDS = 10

data class UploadWordUIState(
    val words: List<SpellingWord> = emptyList(),
    val categories: List<String> = emptyList(),
    val currentWord: EditState = EditState(),
    val currentIndex: Int? = null,
    val message: String = "",
    val state: UIState = UIState.LOADING
) {
    private val isFull: Boolean = words.size == MAX_NR_OF_WORDS

    val isSavable: Boolean = words.isNotEmpty()

    val isExpandable: Boolean = state == UIState.SUCCESS && !isFull

    val getInfoMessage: String = when (state) {
        UIState.ERROR -> message
        UIState.LOADING -> "Loading..."
        UIState.VALIDATION_ERROR -> "Validation Error"
        else -> if (words.isEmpty()) {
            "The are no words"
        } else {
            ""
        }
    }

    val isEditing = state == UIState.EDITING
}

data class EditState(
    val word: SpellingWord = SpellingWord("", "", ""),
    val invalidFields: List<Pair<String, Int>>? = null
)
