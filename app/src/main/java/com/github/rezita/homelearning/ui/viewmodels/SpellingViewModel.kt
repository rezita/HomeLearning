package com.github.rezita.homelearning.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.navigation.SpellingDestination
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.spelling.SpellingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SpellingState {
    LOADING, LOADED, LOAD_ERROR, SAVED, SAVING, SAVING_ERROR,
    EDITING, EDIT_SAVE, EDIT_ERROR
}

class SpellingViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordRepository: WordRepository,

    ) : ViewModel() {

    private val sheetAction: SheetAction =
        checkNotNull(savedStateHandle[SpellingDestination.sheetActionArg])

    private val viewModelState = MutableStateFlow(
        SpellingViewModelState(state = SpellingState.LOADING)
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
        load()
    }

    fun load() {
        when (sheetAction) {
            SheetAction.READ_ERIK_SPELLING_WORDS -> getErikSpellingWords()
            SheetAction.READ_MARK_SPELLING_WORDS -> getMarkSpellingWords()
            else -> viewModelState.update {
                it.copy(
                    state = SpellingState.LOAD_ERROR,
                    words = emptyList(),
                    savingResponse = "",
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun resetUiState() {
        viewModelState.update {
            it.copy(
                state = SpellingState.LOADING,
                words = emptyList(),
                errorMessage = null,
                savingResponse = ""
            )
        }
    }

    private fun getErikSpellingWords() = getSpellingWords { wordRepository.getErikSpellingWords() }

    private fun getMarkSpellingWords() = getSpellingWords { wordRepository.getMarkSpellingWords() }

    private fun getSpellingWords(callback: suspend () -> RepositoryResult<List<SpellingWord>>) {
        resetUiState()
        viewModelScope.launch {
            val result = callback()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(
                        state = SpellingState.LOADED,
                        words = result.data,
                    )
                    //else: RepositoryResult.Error
                    else -> it.copy(
                        state = SpellingState.LOAD_ERROR,
                        errorMessage = R.string.snackBar_error_loading,
                    )
                }
            }
        }
    }

    fun saveSpellingResults() {
        when (sheetAction) {
            SheetAction.READ_ERIK_SPELLING_WORDS -> saveErikSpellingResults()
            SheetAction.READ_MARK_SPELLING_WORDS -> saveMarkSpellingResults()
            else -> viewModelState.update {
                it.copy(
                    state = SpellingState.SAVING_ERROR,
                    words = emptyList(),
                    savingResponse = "",
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun saveErikSpellingResults() =
        uploadSpellingResults { wordRepository.updateErikSpellingWords(it) }

    private fun saveMarkSpellingResults() =
        uploadSpellingResults { wordRepository.updateMarkSpellingWords(it) }


    private fun uploadSpellingResults(callback: suspend (List<SpellingWord>) -> RepositoryResult<String>) {
        if (!viewModelState.value.isSavable()) {
            return
        }

        viewModelState.update {
            it.copy(
                state = SpellingState.SAVING,
                savingResponse = "",
                errorMessage = null,
            )
        }
        viewModelScope.launch {
            val result = callback(viewModelState.value.getSavableWords())
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(
                        state = SpellingState.SAVED,
                        savingResponse = result.data,
                        errorMessage = null,
                    )
                    //else: RepositoryResult.Error
                    else -> it.copy(
                        state = SpellingState.SAVING_ERROR,
                        savingResponse = "",
                        errorMessage = R.string.snackBar_save_error,
                    )
                }
            }
        }
    }

    fun updateWordStatus(index: Int, status: WordStatus) {
        viewModelState.update {
            it.copy(
                words = it.words.toMutableList()
                    .apply { this[index] = this[index].copy(status = status) }
            )
        }
    }

    fun resetWordStatus(index: Int) {
        viewModelState.update {
            it.copy(
                words = it.words.toMutableList()
                    .apply { this[index] = this[index].copy(status = WordStatus.UNCHECKED) }
            )
        }
    }

    fun setForEditing(index: Int) {
        viewModelState.update {
            it.copy(
                state = SpellingState.EDITING,
                editState = SpellingEditState(
                    wordOriginal = it.words[index].word,
                    wordModified = it.words[index].word,
                    index = index
                )
            )
        }
    }

    fun discardEditing() {
        viewModelState.update {
            it.copy(
                state = SpellingState.LOADED,
                editState = SpellingEditState(),
                errorMessage = null
            )
        }
    }

    fun modifyEditedWord(word: String) {
        if (this.viewModelState.value.state !in listOf(
                SpellingState.EDITING,
                SpellingState.EDIT_ERROR
            )
        ) {
            return
        }

        viewModelState.update {
            it.copy(
                state = SpellingState.EDITING,
                editState = it.editState.copy(wordModified = word),
                savingResponse = ""
            )
        }
    }

    fun saveEditing() {
        //save
        when (sheetAction) {
            SheetAction.READ_ERIK_SPELLING_WORDS -> saveEditedErikSpellingWord()
            SheetAction.READ_MARK_SPELLING_WORDS -> saveEditedMarkSpellingWord()
            else -> viewModelState.update {
                it.copy(
                    state = SpellingState.EDIT_ERROR,
                    savingResponse = "Wrong action provided",
                )
            }
        }
    }

    private fun saveEditedErikSpellingWord() =
        saveEditedWord { wordOld, wordNew ->
            wordRepository.modifyErikSpellingWord(wordOld, wordNew)
        }

    private fun saveEditedMarkSpellingWord() =
        saveEditedWord { wordOld, wordNew ->
            wordRepository.modifyMarkSpellingWord(wordOld, wordNew)
        }


    private fun saveEditedWord(callback: suspend (String, String) -> RepositoryResult<String>) {
        if (this.viewModelState.value.state !in listOf(
                SpellingState.EDITING,
                SpellingState.EDIT_ERROR
            )
        ) {
            return
        }
        //change state to Edit_save
        viewModelState.update {
            it.copy(
                state = SpellingState.EDIT_SAVE,
                savingResponse = ""
            )
        }
        val newWord = viewModelState.value.editState.wordModified
        val index = viewModelState.value.editState.index
        viewModelScope.launch {
            val result = callback(
                viewModelState.value.editState.wordOriginal,
                viewModelState.value.editState.wordModified
            )
            viewModelState.update {
                when (result) {
                    //success - update spelling state with the new word
                    is RepositoryResult.Success -> {
                        it.copy(
                            state = SpellingState.LOADED,
                            words = it.words.toMutableList()
                                .apply { this[index!!] = this[index].copy(word = newWord) },
                            errorMessage = null,
                            savingResponse = "",
                            editState = SpellingEditState()
                        )
                    }
                    //else: RepositoryResult.Error
                    is RepositoryResult.Error -> it.copy(
                        state = SpellingState.EDIT_ERROR,
                        savingResponse = result.message,
                    )
                }
            }
        }


        //if successs update wordlist + message + change state
        //if failed: discard changes + message + change state
    }


}

data class SpellingViewModelState(
    val state: SpellingState,
    val words: List<SpellingWord> = emptyList(),
    val errorMessage: Int? = null,
    val savingResponse: String = "",
    val editState: SpellingEditState = SpellingEditState(),
) {
    fun isSavable(): Boolean {
        return when (state) {
            SpellingState.LOADED -> {
                words.isNotEmpty() && words.any { word -> word.status != WordStatus.UNCHECKED }
            }

            SpellingState.SAVING_ERROR -> {
                words.isNotEmpty() && words.any { word -> word.status != WordStatus.UNCHECKED }
            }

            else -> false
        }
    }

    fun getSavableWords(): List<SpellingWord> =
        words.filter { it.status != WordStatus.UNCHECKED }

    fun toUiState(): SpellingUiState =
        when (state) {
            SpellingState.LOADING ->
                SpellingUiState.Loading

            SpellingState.SAVING ->
                SpellingUiState.Loading

            SpellingState.LOADED ->
                SpellingUiState.Loaded(words = words)

            SpellingState.SAVED -> {
                SpellingUiState.Saved(words = words)
            }

            SpellingState.LOAD_ERROR -> {
                SpellingUiState.LoadingError(errorMessage = errorMessage!!)
            }

            SpellingState.SAVING_ERROR -> {
                SpellingUiState.SavingError(
                    errorMessage = errorMessage!!,
                    words = words
                )
            }

            SpellingState.EDITING -> {
                SpellingUiState.Editing(
                    words = words,
                    editState = editState,
                    isEditing = true
                )
            }

            SpellingState.EDIT_SAVE -> {
                SpellingUiState.Editing(
                    words = words,
                    editState = editState,
                    isEditing = false
                )
            }

            SpellingState.EDIT_ERROR -> {
                SpellingUiState.EditError(
                    errorMessage = savingResponse,
                    words = words,
                    editState = editState,
                )
            }
        }
}

data class SpellingEditState(
    val wordOriginal: String = "",
    val wordModified: String = "",
    val index: Int? = null
)