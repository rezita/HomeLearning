package com.github.rezita.homelearning.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.ComplexRepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.SheetAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val MAX_NR_OF_WORDS = 10
const val MAX_WORD_LENGTH = 30
const val MAX_COMMENT_LENGTH = 25
private val wordPattern = Regex("^[a-zA-Z][a-zA-Z\\s'-]{1,35}")
private val commentPattern = Regex("[\\w\\s-']{1,35}")

data class EditState(
    val isEditing: Boolean = false,
    val word: SpellingWord = SpellingWord("", "", ""),
    val invalidFields: List<Pair<String, Int>>? = arrayListOf(),
    val index: Int? = null
)

class UploadWordViewModel(
    private val wordRepository: WordRepository,
    private val sheetAction: SheetAction
) : ViewModel() {

    private val _uploadWordsUIState =
        MutableStateFlow<ComplexRepositoryResult<String, SpellingWord>>(ComplexRepositoryResult.Downloading())
    val uploadWordUIState: StateFlow<ComplexRepositoryResult<String, SpellingWord>>
        get() = _uploadWordsUIState.asStateFlow()

    private var _isFull = MutableStateFlow(false)
    val isFull: StateFlow<Boolean> = _isFull.asStateFlow()

    private var _currentEdited = MutableStateFlow(EditState())
    val currentEdited: StateFlow<EditState> = _currentEdited.asStateFlow()

    val expandable = combine(
        _uploadWordsUIState,
        _isFull,
        currentEdited
    ) { stateValue, fullValue, editedValue ->
        if (editedValue.isEditing || fullValue) {
            false
        } else {
            when (stateValue) {
                is ComplexRepositoryResult.Downloaded -> true
                is ComplexRepositoryResult.Uploaded -> true
                else -> false
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(300),
        initialValue = MutableStateFlow(false)
    )

    init {
        initCurrentEdited()
        load()
    }

    private fun load() {
        resetUiState()
        when (sheetAction) {
            SheetAction.SAVE_ERIK_WORDS -> loadErikCategories()
            SheetAction.SAVE_MARK_WORDS -> loadMarkCategories()
            else -> _uploadWordsUIState.update {
                ComplexRepositoryResult.DownloadingError("Wrong action provided")
            }
        }
    }

    private fun initCurrentEdited() {
        _currentEdited.update { EditState() }
    }

    private fun resetUiState() {
        _uploadWordsUIState.update { ComplexRepositoryResult.Downloading() }
    }

    private fun loadErikCategories() = loadCategories { wordRepository.getErikCategories() }

    private fun loadMarkCategories() = loadCategories { wordRepository.getMarkCategories() }

    fun saveMarkSpellingWords() = saveSpellingWords { uploadable, downloadable ->
        wordRepository.saveMarkSpellingWords(uploadable, downloadable)
    }

    fun saveErikSpellingWords() = saveSpellingWords { uploadable, downloadable ->
        wordRepository.saveErikSpellingWords(uploadable, downloadable)
    }

    private fun loadCategories(callback: suspend () -> ComplexRepositoryResult<String, SpellingWord>) {
        viewModelScope.launch {
            _uploadWordsUIState.emit(callback())
        }
    }

    private fun saveSpellingWords(callback: suspend (List<SpellingWord>, List<String>) -> ComplexRepositoryResult<String, SpellingWord>) {
        when (val state = _uploadWordsUIState.value) {
            is ComplexRepositoryResult.Downloaded -> {
                /**There are no words to upload*/
                val words = state.uploadable
                if (words.isEmpty()) {
                    return
                }
                viewModelScope.launch {
                    _uploadWordsUIState.update {
                        ComplexRepositoryResult.Uploading(
                            downloaded = state.downloaded,
                            uploadable = words
                        )
                    }
                    _uploadWordsUIState.update {
                        callback(
                            words,
                            state.downloaded
                        )
                    }
                }
            }

            else -> return
        }

    }

    fun setForEditing(index: Int? = null, word: SpellingWord = SpellingWord("", "", "")) {
        _currentEdited.update {
            it.copy(
                isEditing = true,
                index = index,
                word = word
            )
        }
    }

    fun removeWord(index: Int) {
        when (val state = _uploadWordsUIState.value) {
            is ComplexRepositoryResult.Downloaded -> {
                val words = state.uploadable.toMutableList()
                if (words.isEmpty()) {
                    return
                }
                words.removeAt(index)
                _uploadWordsUIState.update {
                    ComplexRepositoryResult.Downloaded(
                        downloaded = state.downloaded,
                        uploadable = words
                    )
                }
            }

            else -> return
        }
    }

    fun updateWord() {
        when (val state = _uploadWordsUIState.value) {
            is ComplexRepositoryResult.Downloaded -> {
                if (validateWord()) {
                    //add new word
                    val index = _currentEdited.value.index

                    val words = state.uploadable.toMutableList()
                    when (index) {
                        null -> {
                            words.add(_currentEdited.value.word)
                            _isFull.value = words.size == MAX_NR_OF_WORDS
                        }

                        else -> words[index] = words[index].copy(
                            word = _currentEdited.value.word.word,
                            category = _currentEdited.value.word.category,
                            comment = _currentEdited.value.word.comment
                        )
                    }
                    _uploadWordsUIState.update {
                        ComplexRepositoryResult.Downloaded(
                            uploadable = words,
                            downloaded = state.downloaded
                        )
                    }
                    //init the _currentEdited
                    initCurrentEdited()
                }

            }

            else -> return
        }


    }

    fun cancelUpdate() {
        initCurrentEdited()
    }

    fun updateCurrentWordWord(word: String) {
        val currentWord = _currentEdited.value.word
        val updatedWord = currentWord.copy(word = word)
        _currentEdited.update {
            it.copy(
                word = updatedWord
            )
        }
    }

    fun updateCurrentWordCategory(category: String) {
        val currentWord = _currentEdited.value.word
        val updatedWord = currentWord.copy(category = category)
        _currentEdited.update {
            it.copy(
                word = updatedWord
            )
        }
    }

    fun updateCurrentWordComment(comment: String) {
        val currentWord = _currentEdited.value.word
        val updatedWord = currentWord.copy(comment = comment)
        _currentEdited.update {
            it.copy(
                word = updatedWord
            )
        }
    }

    private fun validateWord(): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        val currentWord = _currentEdited.value.word
        if (!isValidText(currentWord.word, wordPattern)) {
            invalidFields.add(INPUT_WORD)
        }
        if (currentWord.comment.isNotEmpty() &&
            !isValidText(currentWord.comment, commentPattern)
        ) {
            invalidFields.add(INPUT_COMMENT)
        }
        if (currentWord.category.isEmpty()) {
            invalidFields.add(INPUT_CATEGORY)
        }
        if (invalidFields.isNotEmpty()) {
            _currentEdited.update { it.copy(invalidFields = invalidFields) }
            return false
        }
        _currentEdited.update { it.copy(invalidFields = arrayListOf()) }
        return true
    }

    private fun isValidText(text: String, pattern: Regex): Boolean {
        return pattern.matches(text)
    }

    companion object {
        //Validation
        val INPUT_WORD = "WORD" to R.string.upload_dialog_error_word_message
        val INPUT_COMMENT = "INPUT_COMMENT" to R.string.upload_dialog_error_comment_message
        val INPUT_CATEGORY = "INPUT_CATEGORY" to R.string.upload_dialog_error_category_message
    }

    class UploadWordViewModelFactory(
        private val sheetAction: SheetAction
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            return UploadWordViewModel(
                (application as HomeLearningApplication).container.wordRepository,
                sheetAction
            ) as T
        }
    }
}