package com.github.rezita.homelearning.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FillInSentenceViewModel(
    private val wordRepository: WordRepository,
    private val sheetAction: SheetAction
) : ViewModel() {
    /**
     * Store UI state
     */
    private val _sentenceUIState =
        MutableStateFlow<NormalRepositoryResult<FillInSentence>>(NormalRepositoryResult.Downloading())
    val sentenceUIState: StateFlow<NormalRepositoryResult<FillInSentence>> =
        _sentenceUIState.asStateFlow()

    var isAllAnswered by mutableStateOf(false)
        private set

    init {
        load()
    }

    fun load() {
        resetUiState()
        when (sheetAction) {
            SheetAction.READ_IRREGULAR_VERBS -> getIrregularVerbs()
            SheetAction.READ_HOMOPHONES -> getHomophones()
            else -> _sentenceUIState.update {
                NormalRepositoryResult.DownloadingError("Wrong action provided")
            }
        }
    }

    private fun resetUiState() {
        isAllAnswered = false
        _sentenceUIState.update { NormalRepositoryResult.Downloading() }
    }

    fun getIrregularVerbs() = getSentences { wordRepository.getIrregularVerbs() }

    private fun getHomophones() = getSentences { wordRepository.getHomophones() }


    fun saveSentences() {
        when (sheetAction) {
            SheetAction.READ_IRREGULAR_VERBS -> saveIrregularVerbs()
            SheetAction.READ_HOMOPHONES -> saveHomophones()
            else -> _sentenceUIState.update {
                NormalRepositoryResult.UploadError(emptyList(), "Wrong action provided")
            }
        }
    }

    private fun saveIrregularVerbs() =
        saveFillInSentenceResults { wordRepository.updateIrregularVerbs(it) }

    private fun saveHomophones() = saveFillInSentenceResults { wordRepository.updateHomophones(it) }

    private fun getSentences(callback: suspend () -> NormalRepositoryResult<FillInSentence>) {
        viewModelScope.launch {
            _sentenceUIState.emit(callback())
        }
    }

    fun updateAnswer(index: Int, answer: String) {
        when (val state = _sentenceUIState.value) {
            is NormalRepositoryResult.Downloaded -> {
                val sentences =
                    state.data.toMutableList()
                        .apply { this[index] = this[index].copy(answer = answer) }

                _sentenceUIState.update { state.copy(data = sentences) }
                isAllAnswered =  sentences.none { it.status == WordStatus.UNCHECKED }
            }

            else -> return
        }
    }

    private fun saveFillInSentenceResults(callback: suspend (List<FillInSentence>) -> NormalRepositoryResult<FillInSentence>) {
        if (isAllAnswered) {
            when (val state = _sentenceUIState.value) {
                is NormalRepositoryResult.Downloaded -> {
                    viewModelScope.launch {
                        val data = state.data
                        _sentenceUIState.emit(NormalRepositoryResult.Uploading(data))
                        _sentenceUIState.emit(callback(data))
                    }
                }

                is NormalRepositoryResult.UploadError -> {
                    viewModelScope.launch {
                        val data = state.data
                        _sentenceUIState.emit(NormalRepositoryResult.Uploading(data))
                        _sentenceUIState.emit(callback(data))
                    }
                }

                else -> return
            }
        }
    }

    class FillInSentenceViewModelFactory(
        private val sheetAction: SheetAction
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            return FillInSentenceViewModel(
                (application as HomeLearningApplication).container.wordRepository,
                sheetAction
            ) as T
        }
    }
}
