package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.uiState.EditState
import com.github.rezita.homelearning.ui.uiState.UIState
import com.github.rezita.homelearning.ui.uiState.UploadWordUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


const val MAX_WORD_LENGTH = 30
const val MAX_COMMENT_LENGTH = 25
private val wordPattern = Regex("^[a-zA-Z][a-zA-Z\\s'-]{1,35}")
private val commentPattern = Regex("[\\w\\s-']{1,35}")

class UploadWordViewModel(private val wordRepository: WordRepository) : ViewModel() {
    private val _uploadWordsUIState = MutableStateFlow(UploadWordUIState())
    val uploadWordUIState: StateFlow<UploadWordUIState>
        get() = _uploadWordsUIState.asStateFlow()

    fun loadErikCategories() = loadCategories { wordRepository.getErikCategories() }

    fun loadMarkCategories() = loadCategories { wordRepository.getMarkCategories() }

    fun saveMarkSpellingWords() = saveSpellingWords { wordRepository.saveMarkSpellingWords(it) }

    fun saveErikSpellingWords() = saveSpellingWords { wordRepository.saveErikSpellingWords(it) }

    private fun loadCategories(callback: suspend () -> List<String>) {
        viewModelScope.launch {
            try {
                _uploadWordsUIState.update {
                    it.copy(
                        state = UIState.LOADING,
                        message = "",
                        categories = emptyList()
                    )
                }
                val categories = callback()

                _uploadWordsUIState.update {
                    it.copy(
                        categories = categories,
                        state = UIState.SUCCESS,
                        message = ""
                    )
                }
            } catch (e: IOException) {
                Log.i("Error", e.message.toString())
                _uploadWordsUIState.update {
                    it.copy(
                        categories = emptyList(),
                        state = UIState.ERROR,
                        message = "Error"
                    )
                }
            }
        }
    }

    private fun saveSpellingWords(callback: suspend (List<SpellingWord>) -> String) {
        viewModelScope.launch {
            try {
                _uploadWordsUIState.update {
                    it.copy(state = UIState.LOADING)
                }
                val result = callback(uploadWordUIState.value.words)
                _uploadWordsUIState.update {
                    it.copy(
                        words = emptyList(),
                        state = UIState.SUCCESS,
                        message = result,
                        currentWord = EditState()
                    )
                }
                //TODO: notify user if success
            } catch (e: IOException) {
                Log.i("IOException", e.message.toString())
                _uploadWordsUIState.update {
                    it.copy(
                        state = UIState.ERROR,
                        message = "Error while uploading"
                    )
                }
            }
        }
    }

    fun setForEditing(index: Int? = null, word: SpellingWord = SpellingWord("", "", "")) {
        val currentWord = EditState(word = word)
        _uploadWordsUIState.update {
            it.copy(
                currentWord = currentWord,
                state = UIState.EDITING,
                currentIndex = index
            )
        }
    }

    fun removeWord(index: Int) {
        val words = _uploadWordsUIState.value.words.toMutableList()
        words.removeAt(index)
        _uploadWordsUIState.update {
            it.copy(words = words)
        }
    }

    fun updateWord() {
        if (validateWord()) {
            //add new word
            val index = _uploadWordsUIState.value.currentIndex
            val words = _uploadWordsUIState.value.words.toMutableList()
            when (index) {
                null -> words.add(_uploadWordsUIState.value.currentWord.word)
                else -> words[index] = words[index].copy(
                    word = _uploadWordsUIState.value.currentWord.word.word,
                    category = _uploadWordsUIState.value.currentWord.word.category,
                    comment = _uploadWordsUIState.value.currentWord.word.comment
                )
            }
            _uploadWordsUIState.update {
                it.copy(
                    words = words as List<SpellingWord>,
                    currentWord = EditState(),
                    state = UIState.SUCCESS,
                    currentIndex = null
                )
            }
            Log.i("currentWord", uploadWordUIState.value.currentWord.word.toString())
        }
    }

    fun cancelUpdate() {
        _uploadWordsUIState.update {
            it.copy(
                currentWord = EditState(),
                state = UIState.SUCCESS,
                currentIndex = null
            )
        }
    }

    fun updateCurrentWordWord(word: String) {
        val updatedWord = uploadWordUIState.value.currentWord.word.copy(word = word)
        val current = uploadWordUIState.value.currentWord.copy(word = updatedWord)
        _uploadWordsUIState.update { it.copy(currentWord = current) }
    }

    fun updateCurrentWordCategory(category: String) {
        val updatedWord = uploadWordUIState.value.currentWord.word.copy(category = category)
        val current = uploadWordUIState.value.currentWord.copy(word = updatedWord)
        _uploadWordsUIState.update { it.copy(currentWord = current) }
    }

    fun updateCurrentWordComment(comment: String) {
        val updatedWord = uploadWordUIState.value.currentWord.word.copy(comment = comment)
        val current = uploadWordUIState.value.currentWord.copy(word = updatedWord)
        _uploadWordsUIState.update { it.copy(currentWord = current) }

    }

    private fun validateWord(): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        val currentWord = _uploadWordsUIState.value.currentWord
        if (!isValidText(currentWord.word.word, wordPattern)) {
            invalidFields.add(INPUT_WORD)
        }
        if (currentWord.word.comment.isNotEmpty() &&
            !isValidText(currentWord.word.comment, commentPattern)
        ) {
            invalidFields.add(INPUT_COMMENT)
        }
        if (currentWord.word.category.isEmpty()) {
            invalidFields.add(INPUT_CATEGORY)
        }
        if (invalidFields.isNotEmpty()) {
            _uploadWordsUIState.update { it.copy(currentWord = currentWord.copy(invalidFields = invalidFields)) }
            return false
        }
        _uploadWordsUIState.update { it.copy(currentWord = currentWord.copy(invalidFields = null)) }
        return true
    }

    private fun isValidText(text: String, pattern: Regex): Boolean {
        return pattern.matches(text)
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

                return UploadWordViewModel(
                    (application as HomeLearningApplication).container.wordRepository
                ) as T
            }
        }

        //Validation
        val INPUT_WORD = "WORD" to R.string.upload_dialog_error_word_message
        val INPUT_COMMENT = "INPUT_COMMENT" to R.string.upload_dialog_error_comment_message
        val INPUT_CATEGORY = "INPUT_CATEGORY" to R.string.upload_dialog_error_category_message
    }
}

/*
private fun isValidInputs(): Boolean {
        var result = true

        val wordLayout = binding.uploadDialogWordTextLayout
        val wordText = wordLayout.editText?.text.toString().trim()
        if (!isValidText(wordText, _wordPattern)) {
            result = false
            wordLayout.error = getString(R.string.upload_dialog_error_word_message)
            wordLayout.setErrorIconDrawable(0)
        }

        val commentLayout = binding.uploadDialogCommentTextLayout
        val commentText = commentLayout.editText?.text.toString().trim()
        if (commentText.isNotEmpty() && !isValidText(commentText, _commentPattern)) {
            result = false
            commentLayout.error = getString(R.string.upload_dialog_error_comment_message)
            commentLayout.setErrorIconDrawable(0)
        }

        val spinnerLayout = binding.uploadDialogCategoriesTextLayout
        if (getSelectedCategory().isEmpty()) {
            result = false
            spinnerLayout.error = getString(R.string.upload_dialog_error_category_message)
            spinnerLayout.setErrorIconDrawable(0)
        }
        return result
    }



 */