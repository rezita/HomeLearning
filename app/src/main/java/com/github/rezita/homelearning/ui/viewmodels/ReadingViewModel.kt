package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.uiState.ReadingUIState
import com.github.rezita.homelearning.ui.uiState.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ReadingViewModel(
    private val wordRepository: WordRepository,
    private val sheetAction: SheetAction
) : ViewModel() {
    private val _readingUIState = MutableStateFlow(ReadingUIState())
    val readingUIState: StateFlow<ReadingUIState> = _readingUIState.asStateFlow()

    var isColourDisplay by mutableStateOf(true)
        private set

    init {
        load()
    }

    fun load() {
        resetUiState()
        when (sheetAction) {
            SheetAction.READ_READING_CEW -> getCEWWords()
            SheetAction.READ_READING_WORDS -> getReadingWords()
            else ->
                _readingUIState.update {
                    it.copy(
                        words = emptyList(),
                        message = "Wrong action provided",
                        state = UIState.ERROR,
                    )
                }
        }
    }

    fun setColorDisplay(value: Boolean){
        isColourDisplay = value
    }

    private fun resetUiState() {
        _readingUIState.update {
            it.copy(
                words = emptyList(),
                message = "",
                state = UIState.LOADING,
            )
        }
    }

    fun getCEWWords() {
        getWords { wordRepository.getCEWWords() }
    }

    fun getReadingWords() {
        getWords { wordRepository.getReadingWords() }
    }

    private fun getWords(callback: suspend () -> List<ReadingWord>) {
        viewModelScope.launch {
            try {
                resetUiState()
                val words = callback()
                _readingUIState.update {
                    it.copy(
                        words = words,
                        state = UIState.SUCCESS,
                        message = ""
                    )
                }
            } catch (e: IOException) {
                Log.i("IOException", e.message.toString())
                _readingUIState.update {
                    it.copy(
                        words = emptyList(),
                        state = UIState.ERROR,
                        message = "Error"
                    )
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