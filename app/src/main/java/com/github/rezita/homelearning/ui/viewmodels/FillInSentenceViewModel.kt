package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.ui.uiState.FillInSentenceUIState
import com.github.rezita.homelearning.ui.uiState.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class FillInSentenceViewModel(private val wordRepository: WordRepository) : ViewModel() {
    /**
     * Store UI state
     */
    private val _sentenceUIState = MutableStateFlow(FillInSentenceUIState())
    val sentenceUIState: StateFlow<FillInSentenceUIState> = _sentenceUIState.asStateFlow()

    fun getIrregularVerbs() = getSentences { wordRepository.getIrregularVerbs() }

    fun getHomophones() = getSentences { wordRepository.getHomophones() }

    fun saveIrregularVerbs() = checkAnswers { wordRepository.updateIrregularVerbs(it) }

    fun saveHomophones() = checkAnswers { wordRepository.updateHomophones(it) }

    private fun checkAnswers(callback: suspend (List<FillInSentence>) -> String) {
        if (sentenceUIState.value.isAllAnswered) {
            //send to server
            saveFillInSentenceResults(callback)
        }
    }

    private fun getSentences(callback: suspend () -> List<FillInSentence>) {

        viewModelScope.launch {
            try {
                _sentenceUIState.update {
                    it.copy(
                        state = UIState.LOADING,
                        sentences = emptyList(),
                        errorMessage = ""
                    )
                }
                val sentences = callback()
                _sentenceUIState.update {
                    it.copy(
                        state = UIState.SUCCESS,
                        sentences = sentences,
                        errorMessage = ""
                    )
                }
            } catch (e: IOException) {
                Log.i("IOException", e.message.toString())
                _sentenceUIState.update {
                    it.copy(
                        state = UIState.ERROR,
                        sentences = emptyList(),
                        errorMessage = "Error"
                    )
                }
            }
        }
    }

    fun updateAnswer(index: Int, answer: String) {
        val sentences = _sentenceUIState.value.sentences.toMutableList()
        sentences[index] = sentences[index].copy(answer = answer)
        _sentenceUIState.update {
            it.copy(
                sentences = sentences,
            )
        }
    }

    private fun saveFillInSentenceResults(callback: suspend (List<FillInSentence>) -> String) {
        viewModelScope.launch {
            try {
                _sentenceUIState.update { it.copy(state = UIState.LOADING) }
                val result = callback(sentenceUIState.value.sentences)
                Log.i("result", result)
                //TODO: notify user if success
                _sentenceUIState.update {
                    it.copy(
                        state = UIState.CHECKED,
                        errorMessage = result
                    )
                }
            } catch (e: IOException) {
                Log.i("IOException", e.message.toString())
                _sentenceUIState.update {
                    it.copy(
                        state = UIState.ERROR,
                        errorMessage = "Error while uploading"
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

                return FillInSentenceViewModel(
                    (application as HomeLearningApplication).container.wordRepository
                ) as T
            }
        }
    }
}