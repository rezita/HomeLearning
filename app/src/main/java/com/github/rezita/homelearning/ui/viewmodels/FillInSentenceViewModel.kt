package com.github.rezita.homelearning.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.sentence.SentenceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SentenceState {
    LOADING, LOADED, LOAD_ERROR, SAVED, SAVING, SAVING_ERROR
}

class FillInSentenceViewModel(
    private val wordRepository: WordRepository,
    private val sheetAction: SheetAction
) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        SentenceViewModelState(state = SentenceState.LOADING)
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
            SheetAction.READ_IRREGULAR_VERBS -> getIrregularVerbs()
            SheetAction.READ_HOMOPHONES -> getHomophones()
            else -> viewModelState.update {
                it.copy(
                    state = SentenceState.LOAD_ERROR,
                    sentences = emptyList(),
                    savingResponse = "",
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun resetUiState() {
        viewModelState.update {
            it.copy(
                state = SentenceState.LOADING,
                sentences = emptyList(),
                errorMessage = null,
                savingResponse = ""
            )
        }
    }

    private fun getIrregularVerbs() = getSentences { wordRepository.getIrregularVerbs() }

    private fun getHomophones() = getSentences { wordRepository.getHomophones() }


    fun saveSentences() {
        when (sheetAction) {
            SheetAction.READ_IRREGULAR_VERBS -> saveIrregularVerbs()
            SheetAction.READ_HOMOPHONES -> saveHomophones()
            else -> viewModelState.update {
                it.copy(
                    state = SentenceState.SAVING_ERROR,
                    savingResponse = "",
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun saveIrregularVerbs() =
        saveFillInSentenceResults { wordRepository.updateIrregularVerbs(it) }

    private fun saveHomophones() = saveFillInSentenceResults { wordRepository.updateHomophones(it) }

    private fun getSentences(callback: suspend () -> RepositoryResult<List<FillInSentence>>) {
        resetUiState()

        viewModelScope.launch {
            val result = callback()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(
                        state = SentenceState.LOADED,
                        sentences = result.data,
                    )

                    is RepositoryResult.Error -> it.copy(
                        state = SentenceState.LOAD_ERROR,
                        errorMessage = R.string.snackBar_error_loading,
                    )
                }
            }
        }
    }

    fun updateAnswer(index: Int, answer: String) {
        require(index in viewModelState.value.sentences.indices) { "Invalid index value" }
        viewModelState.update {
            it.copy(
                sentences = it.sentences.toMutableList()
                    .apply { this[index] = this[index].copy(answer = answer) }
            )
        }
    }

    private fun saveFillInSentenceResults(callback: suspend (List<FillInSentence>) -> RepositoryResult<String>) {
        if (viewModelState.value.isAllAnswered()) {
            viewModelState.update {
                it.copy(
                    state = SentenceState.SAVING,
                    savingResponse = "",
                    errorMessage = null,
                )
            }
            viewModelScope.launch {
                val result = callback(viewModelState.value.sentences)
                viewModelState.update {
                    when (result) {
                        is RepositoryResult.Success -> it.copy(
                            state = SentenceState.SAVED,
                            savingResponse = "",
                            errorMessage = null,
                        )

                        is RepositoryResult.Error -> it.copy(
                            state = SentenceState.SAVING_ERROR,
                            errorMessage = R.string.snackBar_save_error,
                        )
                    }
                }
            }
        }
    }

    class FillInSentenceViewModelFactory(
        private val repository: WordRepository,
        private val sheetAction: SheetAction
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            return FillInSentenceViewModel(
                repository, sheetAction
            ) as T
        }
    }
}


data class SentenceViewModelState(
    val state: SentenceState,
    val sentences: List<FillInSentence> = emptyList(),
    val errorMessage: Int? = null,
    val savingResponse: String = ""
) {
    fun isAllAnswered(): Boolean {
        return when (state) {
            SentenceState.LOADED -> {
                sentences.none { word -> word.status == WordStatus.UNCHECKED }
            }

            SentenceState.SAVING_ERROR -> {
                sentences.none { word -> word.status == WordStatus.UNCHECKED }
            }

            else -> false
        }
    }

    fun toUiState(): SentenceUiState =
        when (state) {
            SentenceState.LOADING ->
                SentenceUiState.Loading

            SentenceState.SAVING ->
                SentenceUiState.Loading

            SentenceState.LOADED ->
                SentenceUiState.Loaded(sentences = sentences)

            SentenceState.SAVED -> {
                SentenceUiState.Saved(sentences = sentences)
            }

            SentenceState.LOAD_ERROR -> {
                SentenceUiState.LoadingError(errorMessage = errorMessage!!)
            }

            SentenceState.SAVING_ERROR -> {
                SentenceUiState.SavingError(
                    errorMessage = errorMessage!!,
                    sentences = sentences
                )
            }
        }
}