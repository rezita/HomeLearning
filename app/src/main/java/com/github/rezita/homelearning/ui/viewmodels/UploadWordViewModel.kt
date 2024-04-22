package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.rezita.homelearning.HomeLearningApplication
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.uploadwords.UploadUiState
import com.github.rezita.homelearning.ui.screens.uploadwords.edit.EditState
import com.github.rezita.homelearning.utils.toListBySeparator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val MAX_NR_OF_WORDS = 10
const val MAX_WORD_LENGTH = 30
const val MAX_COMMENT_LENGTH = 25
const val RESPONSE_SEPARATOR = ","
const val RESPONSE_INNER_SEPARATOR = ":"

private val wordPattern = Regex("^[a-zA-Z][a-zA-Z\\s'-]{1,35}")
private val commentPattern = Regex("[\\w\\s-']{1,35}")

enum class UploadState {
    LOADING, VIEWING, EDITING, SAVED, LOAD_ERROR, SAVING, SAVING_ERROR
}


class UploadWordViewModel(
    private val wordRepository: WordRepository,
    private val sheetAction: SheetAction
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        UploadViewModelState(state = UploadState.LOADING)
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map { viewModelState.value.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        initCategories()
    }

    /**after saving and before new word will be added*/
    private fun resetViewModelState() {
        if (viewModelState.value.state == UploadState.SAVED) {
            viewModelState.update {
                it.copy(
                    state = UploadState.VIEWING,
                    editState = EditState(),
                    words = emptyList(),
                    selectedIndex = null,
                    errorMessage = null,
                    savingResponse = emptyList(),
                )
            }
        }
    }

    fun initCategories() {
        when (sheetAction) {
            SheetAction.SAVE_ERIK_WORDS -> loadErikCategories()
            SheetAction.SAVE_MARK_WORDS -> loadMarkCategories()
            else -> viewModelState.update {
                it.copy(
                    state = UploadState.LOAD_ERROR,
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun loadErikCategories() = loadCategories { wordRepository.getErikCategories() }
    private fun loadMarkCategories() = loadCategories { wordRepository.getMarkCategories() }


    private fun loadCategories(callback: suspend () -> RepositoryResult<List<String>>) {
        viewModelState.update { it.copy(state = UploadState.LOADING) }

        viewModelScope.launch {
            val result = callback()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(
                        state = UploadState.VIEWING,
                        categories = result.data,
                        errorMessage = null
                    )

                    is RepositoryResult.Error -> it.copy(
                        state = UploadState.LOAD_ERROR,
                        errorMessage = R.string.snackBar_error_loading,
                    )
                }
            }
        }
    }


    fun saveSpellingWords() {
        when (sheetAction) {
            SheetAction.SAVE_ERIK_WORDS -> saveErikSpellingWords()
            SheetAction.SAVE_MARK_WORDS -> saveMarkSpellingWords()
            else -> viewModelState.update {
                it.copy(
                    state = UploadState.SAVING_ERROR,
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    fun saveMarkSpellingWords() = saveSpellingWords { words ->
        wordRepository.saveMarkSpellingWords(words)
    }

    fun saveErikSpellingWords() = saveSpellingWords { words ->
        wordRepository.saveErikSpellingWords(words)
    }

    private fun saveSpellingWords(callback: suspend (List<SpellingWord>) -> RepositoryResult<String>) {
        viewModelState.update { it.copy(state = UploadState.LOADING) }

        viewModelScope.launch {
            val result = callback(viewModelState.value.words)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(
                        state = UploadState.SAVED,
                        savingResponse = parseResponse(result.data),
                        errorMessage = null,
                    )

                    is RepositoryResult.Error -> it.copy(
                        state = UploadState.SAVING_ERROR,
                        errorMessage = R.string.snackBar_save_error,
                    )
                }
            }
        }
    }

    private fun parseResponse(response: String): List<Pair<String, String>> {
        Log.i("Response", response)

        var result: ArrayList<Pair<String, String>> = ArrayList()
        val wordsList = response.toListBySeparator(RESPONSE_SEPARATOR)
        for (word in wordsList) {
            if (word.contains(RESPONSE_INNER_SEPARATOR)) {
                val pair =
                    word.split(RESPONSE_INNER_SEPARATOR).let { Pair(it[0], it.getOrNull(1) ?: "") }
                result.add(pair)
            }
        }
        Log.i("Result", result.toString())
        return result
    }

    fun setForEditing(index: Int? = null) {
        if (viewModelState.value.state == UploadState.SAVED) {
            viewModelState.update {
                it.copy(
                    words = emptyList(),
                    savingResponse = emptyList()
                )
            }
        }

        val editWordState = if (index == null) EditState() else
            EditState(word = viewModelState.value.words[index])
        viewModelState.update {
            it.copy(
                state = UploadState.EDITING,
                editState = editWordState,
                selectedIndex = index
            )
        }
    }

    private fun resetEditing() {
        viewModelState.update {
            it.copy(
                state = UploadState.VIEWING,
                editState = EditState(),
                selectedIndex = null
            )
        }
    }

    fun cancelUpdate() {
        resetEditing()
    }

    fun updateWords() {
        if (!validateWord()) {
            return
        }

        val word = viewModelState.value.editState.word
        if (viewModelState.value.selectedIndex != null) {
            viewModelState.update {
                it.copy(
                    state = UploadState.VIEWING,
                    words = it.words.toMutableList()
                        .apply { this[viewModelState.value.selectedIndex!!] = word }
                )
            }
        } else {
            Log.i("new word:", word.toString())
            viewModelState.update {
                it.copy(
                    state = UploadState.VIEWING,
                    words = it.words + word
                )
            }
        }
        resetEditing()
    }

    fun removeWord(indexSelected: Int) {
        viewModelState.update {
            it.copy(
                words = it.words.filterIndexed { index, _ -> index != indexSelected })
        }
    }

    fun updateCurrentWordWord(word: String) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = it.editState.word.copy(word = word)))
        }
    }

    fun updateCurrentWordCategory(category: String) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = it.editState.word.copy(category = category)))
        }
    }

    fun updateCurrentWordComment(comment: String) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = it.editState.word.copy(comment = comment)))
        }
    }

    fun validateWord(): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        val wordTovalidate = viewModelState.value.editState.word
        if (!isValidText(wordTovalidate.word, wordPattern)) {
            invalidFields.add(EditState.INPUT_WORD)
        }
        if (wordTovalidate.comment.isNotEmpty() &&
            !isValidText(wordTovalidate.comment, commentPattern)
        ) {
            invalidFields.add(EditState.INPUT_COMMENT)
        }
        if (wordTovalidate.category.isEmpty()) {
            invalidFields.add(EditState.INPUT_CATEGORY)
        }

        viewModelState.update { it.copy(editState = it.editState.copy(invalidFields = invalidFields)) }
        return invalidFields.isEmpty()
    }

    private fun isValidText(text: String, pattern: Regex): Boolean {
        return pattern.matches(text)
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

data class UploadViewModelState(
    val state: UploadState,
    val editState: EditState = EditState(),
    val categories: List<String> = emptyList(),
    val words: List<SpellingWord> = emptyList(),
    val selectedIndex: Int? = null,
    val errorMessage: Int? = null,
    val savingResponse: List<Pair<String, String>> = emptyList()
) {
    private fun isSavable(): Boolean {
        return when (state) {
            UploadState.SAVING_ERROR -> true
            UploadState.VIEWING -> !words.isEmpty()
            else -> false
        }
    }

    private fun isExpandable(): Boolean {
        return when (state) {
            UploadState.SAVED -> true
            UploadState.VIEWING -> words.size < MAX_NR_OF_WORDS
            else -> false
        }
    }

    /**
     * Converts this [UploadViewModelState] into a more strongly typed [UploadUiState] for driving
     * the ui.
     */
    fun toUiState(): UploadUiState =
        when (state) {
            UploadState.LOADING ->
                UploadUiState.Loading(
                    isExpandable = isExpandable(),
                    categories = categories,
                    isSavable = isSavable()
                )

            UploadState.VIEWING -> {
                if (words.isEmpty())
                    UploadUiState.NoWords(
                        isExpandable = isExpandable(),
                        categories = categories,
                        isSavable = isSavable()
                    )
                else
                    UploadUiState.HasWords(
                        words = words,
                        isExpandable = isExpandable(),
                        categories = categories,
                        isSavable = isSavable()
                    )
            }

            UploadState.EDITING ->
                UploadUiState.Editing(
                    editState = editState,
                    isExpandable = isExpandable(),
                    categories = categories,
                    isSavable = isSavable()
                )

            UploadState.SAVED ->
                UploadUiState.Saved(
                    words = words,
                    isExpandable = isExpandable(),
                    categories = categories,
                    isSavable = isSavable(),
                    savingResult = savingResponse
                )

            UploadState.SAVING ->
                UploadUiState.Saving(
                    words = words,
                    isExpandable = isExpandable(),
                    categories = categories,
                    isSavable = isSavable()
                )

            UploadState.LOAD_ERROR ->
                UploadUiState.LoadingError(
                    errorMessage = errorMessage,
                    isExpandable = isExpandable(),
                    categories = categories,
                    isSavable = isSavable()
                )

            UploadState.SAVING_ERROR ->
                UploadUiState.SavingError(
                    words = words,
                    errorMessage = errorMessage,
                    isExpandable = isExpandable(),
                    categories = categories,
                    isSavable = isSavable()
                )
        }
}