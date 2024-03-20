package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.ui.uiState.ReadingUIState
import com.github.rezita.homelearning.ui.uiState.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ReadingViewModel(private val wordRepository: WordRepository) : ViewModel() {
    private val _readingUIState = MutableStateFlow(ReadingUIState())
    val readingUIState: StateFlow<ReadingUIState> = _readingUIState.asStateFlow()

    fun getReadingWords() {
        getWords { wordRepository.getReadingWords() }
    }

    fun getCEWWords() {
        getWords { wordRepository.getCEWWords() }
    }

    private fun getWords(callback: suspend () -> List<ReadingWord>) {
        viewModelScope.launch {
            try {
                _readingUIState.update {
                    it.copy(
                        state = UIState.LOADING,
                        words = emptyList(),
                        message = ""
                    )
                }
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

    /*
        companion object {
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as HomeLearningApplication)
                    val readingWordRepository = application.container.readingWordRepository
                    ReadingViewModel(readingWordRepository = readingWordRepository)
                }
            }
        }

     */

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return ReadingViewModel(
                    (application as HomeLearningApplication).container.wordRepository
                ) as T
            }
        }
    }
}