package com.github.rezita.homelearning.ui.viewmodels

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

    //var isAllAnswered by mutableStateOf(false)
    var _isAllAnswered = MutableStateFlow(false)
    val isAllAnswered: StateFlow<Boolean> = _isAllAnswered.asStateFlow()

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
        _sentenceUIState.update { NormalRepositoryResult.Downloading() }
    }

    fun getIrregularVerbs() = getSentences { wordRepository.getIrregularVerbs() }

    fun getHomophones() = getSentences { wordRepository.getHomophones() }

    fun saveIrregularVerbs() = saveFillInSentenceResults { wordRepository.updateIrregularVerbs(it) }

    fun saveHomophones() = saveFillInSentenceResults { wordRepository.updateHomophones(it) }

    private fun getSentences(callback: suspend () -> NormalRepositoryResult<FillInSentence>) {
        viewModelScope.launch {
            _sentenceUIState.emit(callback())
        }
    }

    fun updateAnswer(index: Int, answer: String) {
        if (_sentenceUIState.value is NormalRepositoryResult.Downloaded) {
            val sentences = _sentenceUIState.value.data.toMutableList()
            sentences[index] = sentences[index].copy(answer = answer)
            _sentenceUIState.update { NormalRepositoryResult.Downloaded(sentences) }
            _isAllAnswered.update {
                sentences.none { it.status == WordStatus.UNCHECKED }
            }
        }
    }

    private fun saveFillInSentenceResults(callback: suspend (List<FillInSentence>) -> NormalRepositoryResult<FillInSentence>) {
        when (_sentenceUIState.value) {
            is NormalRepositoryResult.Downloaded -> {
                if (_isAllAnswered.value) {
                    viewModelScope.launch {
                        val data = _sentenceUIState.value.data
                        _sentenceUIState.emit(NormalRepositoryResult.Uploading(data))
                        _sentenceUIState.emit(callback(data))
                    }
                }
            }

            is NormalRepositoryResult.UploadError -> {
                if (_isAllAnswered.value) {
                    viewModelScope.launch {
                        val data = _sentenceUIState.value.data
                        _sentenceUIState.emit(NormalRepositoryResult.Uploading(data))
                        _sentenceUIState.emit(callback(data))
                    }
                }
            }

            else -> return
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
