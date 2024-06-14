package com.github.rezita.homelearning.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.spelling.SpellingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SpellingState {
    LOADING, LOADED, LOAD_ERROR, SAVED, SAVING, SAVING_ERROR
}

class SpellingViewModel(
    private val wordRepository: WordRepository,
    private val sheetAction: SheetAction
) : ViewModel() {

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
        saveSpellingResults { wordRepository.updateErikSpellingWords(it) }

    private fun saveMarkSpellingResults() =
        saveSpellingResults { wordRepository.updateMarkSpellingWords(it) }

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

                    is RepositoryResult.Error -> it.copy(
                        state = SpellingState.LOAD_ERROR,
                        errorMessage = R.string.snackBar_error_loading,
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

    private fun saveSpellingResults(callback: suspend (List<SpellingWord>) -> RepositoryResult<String>) {
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

                    is RepositoryResult.Error -> it.copy(
                        state = SpellingState.SAVING_ERROR,
                        savingResponse = "",
                        errorMessage = R.string.snackBar_save_error,
                    )
                }
            }
        }
    }


    class SpellingViewModelFactory(
        private val sheetAction: SheetAction
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            return SpellingViewModel(
                (application as HomeLearningApplication).container.wordRepository,
                sheetAction
            ) as T
        }
    }
}

data class SpellingViewModelState(
    val state: SpellingState,
    val words: List<SpellingWord> = emptyList(),
    val errorMessage: Int? = null,
    val savingResponse: String = ""
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
        }
}