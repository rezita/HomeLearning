package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.uiState.SpellingUIState
import com.github.rezita.homelearning.ui.uiState.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SpellingViewModel(private val wordRepository: WordRepository) : ViewModel() {
    private val _spellingUIState = MutableStateFlow(SpellingUIState())
    val spellingUIState: StateFlow<SpellingUIState>
        get() = _spellingUIState.asStateFlow()

    fun getErikSpellingWords() = getSpellingWords { wordRepository.getErikSpellingWords() }

    fun getMarkSpellingWords() = getSpellingWords { wordRepository.getMarkSpellingWords() }

    fun saveErikSpellingResults() =
        saveSpellingResults { wordRepository.updateErikSpellingWords(it) }

    fun saveMarkSpellingResults() =
        saveSpellingResults { wordRepository.updateMarkSpellingWords(it) }

    private fun getSpellingWords(callback: suspend () -> List<SpellingWord>) {
        viewModelScope.launch {
            try {
                _spellingUIState.update {
                    it.copy(
                        state = UIState.LOADING,
                        words = emptyList(),
                        message = ""
                    )
                }
                val words = callback()
                _spellingUIState.update {
                    it.copy(
                        state = UIState.SUCCESS,
                        words = words,
                    )
                }
            } catch (e: IOException) {
                Log.i("IOException", e.message.toString())
                _spellingUIState.update {
                    it.copy(
                        state = UIState.ERROR,
                        words = emptyList(),
                        message = "Error"
                    )
                }
            }
        }
    }

    fun updateWordStatus(index: Int, status: WordStatus) {
        val updatedWords = _spellingUIState.value.words.toMutableList()
            .apply { this[index] = this[index].copy(status = status) }
        _spellingUIState.update {
            it.copy(
                words = updatedWords,
            )
        }
    }

    private fun saveSpellingResults(callback: suspend (List<SpellingWord>) -> String) {
        viewModelScope.launch {
            try {
                _spellingUIState.update {
                    it.copy(
                        state = UIState.LOADING,
                        message = ""
                    )
                }
                val result = callback(spellingUIState.value.words)
                //TODO: notify user if success
                _spellingUIState.update {
                    it.copy(
                        state = UIState.SUCCESS,
                        words = emptyList(),
                        message = result
                    )
                }
            } catch (e: IOException) {
                Log.i("IOException", e.message.toString())
                _spellingUIState.update {
                    it.copy(
                        state = UIState.ERROR,
                        message = "Error while uploading"
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return SpellingViewModel(
                    (application as HomeLearningApplication).container.wordRepository
                ) as T
            }
        }
    }
}