package com.github.rezita.homelearning.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.reading.ReadingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReadingViewModel(
    private val wordRepository: WordRepository,
    private val sheetAction: SheetAction
) : ViewModel() {
    private val _readingUIState =
        MutableStateFlow<ReadingUiState>(ReadingUiState.Loading)
    var isColourDisplay by mutableStateOf(true)
        private set

    val readingUIState: StateFlow<ReadingUiState> =
        _readingUIState.asStateFlow()

    init {
        load()
    }

    fun load() {
        resetUiState()
        when (sheetAction) {
            SheetAction.READ_READING_CEW -> getCEWWords()
            SheetAction.READ_READING_WORDS -> getReadingWords()
            else -> _readingUIState.update { ReadingUiState.LoadingError(errorMessage = R.string.msg_wrong_action) }
        }
    }

    fun setColorDisplay(value: Boolean) {
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
        if (_readingUIState.value is ReadingUiState.Downloaded) return

        _readingUIState.update { ReadingUiState.Loading }

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

    class ReadingWordViewModelFactory(
        private val sheetAction: SheetAction
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[APPLICATION_KEY])
            return ReadingViewModel(
                (application as HomeLearningApplication).container.wordRepository,
                sheetAction
            ) as T
        }
    }
}