package com.github.rezita.homelearning.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpellingViewModel(
    private val wordRepository: WordRepository,
    private val sheetAction: SheetAction
) : ViewModel() {

    private val _spellingUIState =
        MutableStateFlow<NormalRepositoryResult<SpellingWord>>(NormalRepositoryResult.Downloading())
    val spellingUIState: StateFlow<NormalRepositoryResult<SpellingWord>>
        get() = _spellingUIState.asStateFlow()

    init {
        load()
    }

    fun load() {
        resetUiState()
        when (sheetAction) {
            SheetAction.READ_ERIK_SPELLING_WORDS -> getErikSpellingWords()
            SheetAction.READ_MARK_SPELLING_WORDS -> getMarkSpellingWords()
            else -> _spellingUIState.update {
                NormalRepositoryResult.DownloadingError("Wrong action provided")
            }
        }
    }

    fun resetUiState() {
        _spellingUIState.update { NormalRepositoryResult.Downloading() }
    }

    fun getErikSpellingWords() = getSpellingWords { wordRepository.getErikSpellingWords() }

    fun getMarkSpellingWords() = getSpellingWords { wordRepository.getMarkSpellingWords() }

    fun saveErikSpellingResults() =
        saveSpellingResults { wordRepository.updateErikSpellingWords(it) }

    fun saveMarkSpellingResults() =
        saveSpellingResults { wordRepository.updateMarkSpellingWords(it) }

    private fun getSpellingWords(callback: suspend () -> NormalRepositoryResult<SpellingWord>) {
        viewModelScope.launch {
            _spellingUIState.emit(callback())
        }
    }

    fun updateWordStatus(index: Int, status: WordStatus) {
        when (val state = _spellingUIState.value) {
            is NormalRepositoryResult.Downloaded -> {
                val updatedWords = state.data.toMutableList()
                    .apply { this[index] = this[index].copy(status = status) }
                //_spellingUIState.update { NormalRepositoryResult.Downloaded(updatedWords) }
                _spellingUIState.update { state.copy(data = updatedWords) }
            }

            else -> return
        }
    }

    private fun saveSpellingResults(callback: suspend (List<SpellingWord>) -> NormalRepositoryResult<SpellingWord>) {
        when (val state = _spellingUIState.value) {
            is NormalRepositoryResult.Downloaded -> {
                viewModelScope.launch {
                    val data = state.data
                    _spellingUIState.emit(NormalRepositoryResult.Uploading(data))
                    _spellingUIState.emit(callback(data))
                }
            }

            is NormalRepositoryResult.UploadError -> {
                viewModelScope.launch {
                    val data = state.data
                    _spellingUIState.emit(NormalRepositoryResult.Uploading(data))
                    _spellingUIState.emit(callback(data))
                }
            }

            else -> return
        }
    }

    class SpellingViewModelFactory(
        private val sheetAction: SheetAction
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            return SpellingViewModel(
                (application as HomeLearningApplication).container.wordRepository,
                sheetAction
            ) as T
        }
    }
}