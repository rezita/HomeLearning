package com.github.rezita.homelearning.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.navigation.ReadingDestination
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.reading.ReadingUiState
import com.github.rezita.homelearning.ui.screens.reading.ReadingUserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReadingViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordRepository: WordRepository,
) : ViewModel() {

    private val sheetAction: SheetAction =
        savedStateHandle.toRoute<ReadingDestination>().sheetAction

    private val _readingUIState =
        MutableStateFlow<ReadingUiState>(ReadingUiState.Loading)
    var isColourDisplay by mutableStateOf(true)
        private set

    val readingUIState: StateFlow<ReadingUiState> =
        _readingUIState.asStateFlow()

    init {
        load()
    }

    fun onEvent(event: ReadingUserEvent){
        when(event){
            ReadingUserEvent.OnLoad -> load()
            is ReadingUserEvent.OnChangeColorDisplay ->setColorDisplay(event.visible)
        }
    }

    private fun load() {
        when (sheetAction) {
            SheetAction.READ_READING_CEW -> getCEWWords()
            SheetAction.READ_READING_WORDS -> getReadingWords()
            else -> _readingUIState.update { ReadingUiState.LoadingError(errorMessage = R.string.msg_wrong_action) }
        }
    }

    private fun setColorDisplay(value: Boolean) {
        isColourDisplay = value
    }

    private fun resetUiState() {
        _readingUIState.update { ReadingUiState.Loading }
    }

    private fun getCEWWords() {
        getWords { wordRepository.getCEWWords() }
    }

    private fun getReadingWords() {
        getWords { wordRepository.getReadingWords() }
    }

    private fun getWords(callback: suspend () -> RepositoryResult<List<ReadingWord>>) {
        //if (_readingUIState.value is ReadingUiState.Downloaded) return
        resetUiState()

        viewModelScope.launch {
            val result = callback()
            _readingUIState.update {
                when (result) {
                    is RepositoryResult.Success ->
                        ReadingUiState.Downloaded(result.data)

                    is RepositoryResult.Error ->
                        ReadingUiState.LoadingError(errorMessage = R.string.snackBar_error_loading)
                }
            }
        }
    }
}