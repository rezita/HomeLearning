package com.github.rezita.homelearning.ui.viewmodels

import android.speech.tts.TextToSpeech
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.navigation.SpanishDestination
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.tts.HLTextToSpeech
import com.github.rezita.homelearning.ui.screens.spanish.SpanishUiState
import com.github.rezita.homelearning.ui.screens.spanish.SpanishUserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

enum class SpanishState {
    LOADING, LOADED, LOAD_ERROR, SAVED, SAVING, SAVING_ERROR
}

class SpanishViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordRepository: WordRepository,
    private val tts: HLTextToSpeech
) : ViewModel() {
    private val sheetAction: SheetAction =
        savedStateHandle.toRoute<SpanishDestination>().sheetAction

    private val enToSp: Boolean? =
        savedStateHandle.toRoute<SpanishDestination>().enToSp

    private val viewModelState = MutableStateFlow(
        SpanishViewModelState(state = SpanishState.LOADING)
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

    fun onEvent(event: SpanishUserEvent) {
        when (event) {
            SpanishUserEvent.OnLoad -> load()
            SpanishUserEvent.OnSave -> saveWords()
            is SpanishUserEvent.OnValueChange -> {
                updateAnswer(event.index, event.value)
            }

            is SpanishUserEvent.OnSpeakerClicked -> {
                tts.speak(event.word, TextToSpeech.QUEUE_ADD, Locale("es", "ES"))
            }

        }
    }


    fun load() {
        when (sheetAction) {
            SheetAction.READ_ZITA_SPANISH_WORDS -> getZitaWords()
            SheetAction.READ_WEEK_SPANISH_WORDS -> getWeekWords()
            else -> viewModelState.update {
                it.copy(
                    state = SpanishState.LOAD_ERROR,
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
                state = SpanishState.LOADING,
                words = emptyList(),
                errorMessage = null,
                savingResponse = ""
            )
        }
    }

    private fun getZitaWords() = getWords { wordRepository.getZitaSpanishWords(enToSp) }

    private fun getWeekWords() = getWords { wordRepository.getWeekSpanishWords() }

    private fun getWords(callback: suspend () -> RepositoryResult<List<SpanishWord>>) {
        resetUiState()

        viewModelScope.launch {
            val result = callback()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(
                        state = SpanishState.LOADED,
                        words = result.data,
                    )

                    is RepositoryResult.Error -> it.copy(
                        state = SpanishState.LOAD_ERROR,
                        errorMessage = R.string.snackBar_error_loading,
                    )
                }
            }
        }
    }

    private fun updateAnswer(index: Int, answer: String) {
        require(index in viewModelState.value.words.indices) { "Invalid index value" }
        viewModelState.update {
            it.copy(
                words = it.words.toMutableList()
                    .apply { this[index] = this[index].copy(answer = answer) }
            )
        }
    }


    private fun saveWords() {
        when (sheetAction) {
            SheetAction.READ_ZITA_SPANISH_WORDS -> saveZitaWords()
            SheetAction.READ_WEEK_SPANISH_WORDS -> saveWeekWords()
            else -> viewModelState.update {
                it.copy(
                    state = SpanishState.SAVING_ERROR,
                    savingResponse = "",
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun saveZitaWords() =
        saveSpanishReadResults { wordRepository.updateZitaSpanishWords(it) }

    //don't save in the repo, just show the results
    private fun saveWeekWords() = {
        if (viewModelState.value.isAllAnswered()) {
            viewModelState.update {
                it.copy(
                    state = SpanishState.SAVED,
                    savingResponse = "",
                    errorMessage = null,
                )
            }
        }
    }

    //We don't save week words results!!!!!
    private fun saveSpanishReadResults(callback: suspend (List<SpanishWord>) -> RepositoryResult<String>) {
        if (viewModelState.value.isAllAnswered()) {
            viewModelState.update {
                it.copy(
                    state = SpanishState.SAVING,
                    savingResponse = "",
                    errorMessage = null,
                )
            }
            viewModelScope.launch {
                val result = callback(viewModelState.value.words)
                viewModelState.update {
                    when (result) {
                        is RepositoryResult.Success -> it.copy(
                            state = SpanishState.SAVED,
                            savingResponse = "",
                            errorMessage = null,
                        )

                        is RepositoryResult.Error -> it.copy(
                            state = SpanishState.SAVING_ERROR,
                            errorMessage = R.string.snackBar_save_error,
                        )
                    }
                }
            }
        }
    }

}

data class SpanishViewModelState(
    val state: SpanishState,
    val words: List<SpanishWord> = emptyList(),
    val errorMessage: Int? = null,
    val savingResponse: String = ""
) {
    fun isAllAnswered(): Boolean {
        return when (state) {
            SpanishState.LOADED -> {
                words.none { word -> word.status == WordStatus.UNCHECKED }
            }

            SpanishState.SAVING_ERROR -> {
                words.none { word -> word.status == WordStatus.UNCHECKED }
            }

            else -> false
        }
    }

    fun toUiState(): SpanishUiState =
        when (state) {
            SpanishState.LOADING ->
                SpanishUiState.Loading

            SpanishState.SAVING ->
                SpanishUiState.Loading

            SpanishState.LOADED ->
                SpanishUiState.Loaded(words = words)

            SpanishState.SAVED -> {
                SpanishUiState.Saved(words = words)
            }

            SpanishState.LOAD_ERROR -> {
                SpanishUiState.LoadingError(errorMessage = errorMessage!!)
            }

            SpanishState.SAVING_ERROR -> {
                SpanishUiState.SavingError(
                    errorMessage = errorMessage!!,
                    words = words
                )
            }
        }
}