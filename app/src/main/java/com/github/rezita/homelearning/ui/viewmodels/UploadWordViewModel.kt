package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.uploadwords.UploadUiState
import com.github.rezita.homelearning.ui.screens.uploadwords.edit.EditState
import com.github.rezita.homelearning.utils.toListBySeparator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val MAX_NR_OF_WORDS = 10
const val MAX_WORD_LENGTH = 30
const val MAX_COMMENT_LENGTH = 25
const val RESPONSE_SEPARATOR = ","
const val RESPONSE_INNER_SEPARATOR = ":"

private val wordPattern = Regex("^[a-zA-Z][a-zA-Z\\s'-()]{1,35}")
private val commentPattern = Regex("[\\w\\s-']{1,35}")

enum class UploadState {
    LOADING, VIEWING, EDITING, SAVED, LOAD_ERROR, SAVING, SAVING_ERROR
}


class UploadWordViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordRepository: WordRepository,
) : ViewModel() {

    private val sheetAction: SheetAction = checkNotNull(savedStateHandle["sheetAction"])

    private val viewModelState = MutableStateFlow(
        UploadViewModelState(state = UploadState.LOADING)
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map { viewModelState.value.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        initCategories()
    }

    /**after saving and before new word will be added*/
    private fun resetViewModelState() {
        if (viewModelState.value.state == UploadState.SAVED) {
            viewModelState.update {
                it.copy(
                    state = UploadState.VIEWING,
                    editState = EditState(),
                    words = emptyList(),
                    selectedIndex = null,
                    errorMessage = null,
                    savingResponse = emptyList(),
                )
            }
        }
    }

    fun initCategories() {
        when (sheetAction) {
            SheetAction.SAVE_ERIK_WORDS -> loadErikCategories()
            SheetAction.SAVE_MARK_WORDS -> loadMarkCategories()
            else -> viewModelState.update {
                it.copy(
                    state = UploadState.LOAD_ERROR,
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun loadErikCategories() = loadCategories { wordRepository.getErikCategories() }
    private fun loadMarkCategories() = loadCategories { wordRepository.getMarkCategories() }


    private fun loadCategories(callback: suspend () -> RepositoryResult<List<String>>) {
        viewModelState.update { it.copy(state = UploadState.LOADING) }

        viewModelScope.launch {
            val result = callback()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(
                        state = UploadState.VIEWING,
                        categories = result.data,
                        errorMessage = null
                    )

                    is RepositoryResult.Error -> it.copy(
                        state = UploadState.LOAD_ERROR,
                        errorMessage = R.string.snackBar_error_loading,
                    )
                }
            }
        }
    }


    fun saveSpellingWords() {
        when (sheetAction) {
            SheetAction.SAVE_ERIK_WORDS -> saveErikSpellingWords()
            SheetAction.SAVE_MARK_WORDS -> saveMarkSpellingWords()
            else -> viewModelState.update {
                it.copy(
                    state = UploadState.SAVING_ERROR,
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun saveMarkSpellingWords() = saveSpellingWords { words ->
        wordRepository.saveMarkSpellingWords(words)
    }

    private fun saveErikSpellingWords() = saveSpellingWords { words ->
        wordRepository.saveErikSpellingWords(words)
    }

    private fun saveSpellingWords(callback: suspend (List<SpellingWord>) -> RepositoryResult<String>) {
        viewModelState.update { it.copy(state = UploadState.LOADING) }

        viewModelScope.launch {
            val result = callback(viewModelState.value.words)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(
                        state = UploadState.SAVED,
                        savingResponse = parseResponse(result.data),
                        errorMessage = null,
                    )

                    is RepositoryResult.Error -> it.copy(
                        state = UploadState.SAVING_ERROR,
                        errorMessage = R.string.snackBar_save_error,
                    )
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseResponse(response: String): List<Pair<SpellingWord, String>> {
        Log.i("Response", response)

        val result: ArrayList<Pair<SpellingWord, String>> = ArrayList()
        val wordsList = response.toListBySeparator(RESPONSE_SEPARATOR)
        for (word in wordsList) {
            if (word.contains(RESPONSE_INNER_SEPARATOR)) {
                val pair =
                    word.split(RESPONSE_INNER_SEPARATOR)
                        .let { Pair(findWordInList(it[0]), it.getOrNull(1) ?: "") }
                pair.takeIf { it.first != null }
                    .let { result.add(pair as Pair<SpellingWord, String>) }
            }
        }
        Log.i("Result", result.toString())
        return result
    }

    private fun findWordInList(word: String): SpellingWord? {
        return viewModelState.value.words.find { it.word == word }
    }

    fun setForEditing(index: Int? = null) {
        if (viewModelState.value.state == UploadState.SAVED) {
            resetViewModelState()
        }

        val editWordState = if (index == null) {
            //set the category for the drop down menu
            if (viewModelState.value.categories.isNotEmpty()) {
                EditState(viewModelState.value.categories[0])
            } else {
                EditState()
            }
        } else {
            EditState(word = viewModelState.value.words[index])
        }
        viewModelState.update {
            it.copy(
                state = UploadState.EDITING,
                editState = editWordState,
                selectedIndex = index
            )
        }
    }

    private fun resetEditing() {
        viewModelState.update {
            it.copy(
                state = UploadState.VIEWING,
                editState = EditState(),
                selectedIndex = null
            )
        }
    }

    fun cancelUpdate() {
        resetEditing()
    }

    fun updateWords() {
        if (!validateWord()) {
            return
        }

        val word = viewModelState.value.editState.word
        if (viewModelState.value.selectedIndex != null) {
            viewModelState.update {
                it.copy(
                    state = UploadState.VIEWING,
                    words = it.words.toMutableList()
                        .apply { this[viewModelState.value.selectedIndex!!] = word }
                )
            }
        } else {
            Log.i("new word:", word.toString())
            viewModelState.update {
                it.copy(
                    state = UploadState.VIEWING,
                    words = it.words + word
                )
            }
        }
        resetEditing()
    }

    fun removeWord(indexSelected: Int) {
        viewModelState.update {
            it.copy(
                words = it.words.filterIndexed { index, _ -> index != indexSelected })
        }
    }

    fun updateCurrentWord(word: SpellingWord) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = word))
        }
    }

    private fun validateWord(): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        val wordToValidate = viewModelState.value.editState.word
        if (!isValidText(wordToValidate.word, wordPattern)) {
            invalidFields.add(EditState.INPUT_WORD)
        }
        if (wordToValidate.comment.isNotEmpty() &&
            !isValidText(wordToValidate.comment, commentPattern)
        ) {
            invalidFields.add(EditState.INPUT_COMMENT)
        }
        if (wordToValidate.category.isEmpty()) {
            invalidFields.add(EditState.INPUT_CATEGORY)
        }

        viewModelState.update { it.copy(editState = it.editState.copy(invalidFields = invalidFields)) }
        return invalidFields.isEmpty()
    }

    private fun isValidText(text: String, pattern: Regex): Boolean {
        return pattern.matches(text)
    }

}

data class UploadViewModelState(
    val state: UploadState,
    val editState: EditState = EditState(),
    val categories: List<String> = emptyList(),
    val words: List<SpellingWord> = emptyList(),
    val selectedIndex: Int? = null,
    val errorMessage: Int? = null,
    val savingResponse: List<Pair<SpellingWord, String>> = emptyList()
) {
    /**
     * Converts this [UploadViewModelState] into a more strongly typed [UploadUiState] for driving
     * the ui.
     */
    fun toUiState(): UploadUiState =
        when (state) {
            UploadState.LOADING ->
                UploadUiState.Loading

            UploadState.VIEWING -> {
                if (words.isEmpty())
                    UploadUiState.NoWords
                else
                    UploadUiState.HasWords(words = words)
            }

            UploadState.EDITING ->
                UploadUiState.Editing(
                    editState = editState,
                    categories = categories,
                )

            UploadState.SAVED ->
                UploadUiState.Saved(
                    words = words,
                    savingResult = savingResponse
                )

            UploadState.SAVING ->
                UploadUiState.Saving

            UploadState.LOAD_ERROR ->
                UploadUiState.LoadingError(
                    errorMessage = errorMessage,
                )

            UploadState.SAVING_ERROR ->
                UploadUiState.SavingError(
                    words = words,
                    errorMessage = errorMessage
                )
        }
}