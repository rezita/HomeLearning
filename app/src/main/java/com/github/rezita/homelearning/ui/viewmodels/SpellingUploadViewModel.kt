package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.navigation.SpellingUploadDestination
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.upload.common.SpellingUploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.UploadState
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.commentPattern
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditState
import com.github.rezita.homelearning.ui.screens.upload.common.isValidText
import com.github.rezita.homelearning.ui.screens.upload.common.parseResponse
import com.github.rezita.homelearning.ui.screens.upload.common.wordPattern
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpellingUploadViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordRepository: WordRepository,
) : ViewModel() {

    private val sheetAction: SheetAction =
        savedStateHandle.toRoute<SpellingUploadDestination>().sheetAction

    private val viewModelState = MutableStateFlow(
        SpellingUploadViewModelState(state = UploadState.LOADING)
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

    fun onUserEvent(event: SpellingUploadUserEvent) {
        when (event) {
            UploadUserEvent.OnSave -> saveSpellingWords()
            UploadUserEvent.OnAddNew -> setForEditing(null)
            UploadUserEvent.OnCancelEditing -> resetEditing()
            UploadUserEvent.OnSaveEditedWord -> updateWordsAfterEditing()
            SpellingUploadUserEvent.OnLoad -> initCategories()
            is UploadUserEvent.OnRemoveWord -> removeWord(event.index)
            is UploadUserEvent.OnPrepareForEditing -> setForEditing(event.index)
            is SpellingUploadUserEvent.OnWordChangeForEditedWord -> updateEditedWord(event.word)
            is SpellingUploadUserEvent.OnCategoryChangeForEditedWord -> updateCategory(event.category)
            is SpellingUploadUserEvent.OnCommentChangeForEditedWord -> updateComment(event.comment)
        }
    }

    /**after saving and before new word will be added*/
    private fun resetViewModelState() {
        if (viewModelState.value.state == UploadState.SAVED) {
            viewModelState.update {
                it.copy(
                    state = UploadState.VIEWING,
                    editState = EditState(SpellingWord("", "", "")),
                    words = emptyList(),
                    selectedIndex = null,
                    errorMessage = null,
                    savingResponse = emptyList(),
                )
            }
        }
    }

    private fun initCategories() {
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

    private fun saveSpellingWords() {
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

    private fun saveMarkSpellingWords() = saveSpellingWords { words ->
        wordRepository.saveMarkSpellingWords(words)
    }

    private fun saveErikSpellingWords() = saveSpellingWords { words ->
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
                        savingResponse = parseResponse(result.data, viewModelState.value.words),
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

    private fun setForEditing(index: Int? = null) {
        if (viewModelState.value.state == UploadState.SAVED) {
            resetViewModelState()
        }

        val editWordState = if (index == null) {
            //set the category for the drop down menu
            if (viewModelState.value.categories.isNotEmpty()) {
                val category = viewModelState.value.categories[0]
                EditState(SpellingWord("", category, ""))
            } else {
                EditState(SpellingWord("", "", ""))
            }
        } else {
            EditState(word = viewModelState.value.words[index])
        }
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
                editState = EditState(SpellingWord("", "", "")),
                selectedIndex = null
            )
        }
    }

    private fun updateWordsAfterEditing() {
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

    private fun removeWord(indexSelected: Int) {
        viewModelState.update {
            it.copy(
                words = it.words.filterIndexed { index, _ -> index != indexSelected })
        }
    }

    private fun updateEditedWord(word: String) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = it.editState.word.copy(word = word)))
        }
    }

    private fun updateCategory(category: String) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = it.editState.word.copy(category = category)))
        }
    }

    private fun updateComment(comment: String) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = it.editState.word.copy(comment = comment)))
        }
    }

    private fun validateWord(): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        val wordToValidate = viewModelState.value.editState.word
        if (!isValidText(wordToValidate.word, wordPattern)) {
            invalidFields.add(EditState.INPUT_WORD)
        }
        if (wordToValidate.comment.isNotEmpty() &&
            !isValidText(wordToValidate.comment, commentPattern)
        ) {
            invalidFields.add(EditState.INPUT_COMMENT)
        }
        if (wordToValidate.category.isEmpty()) {
            invalidFields.add(EditState.INPUT_CATEGORY)
        }

        viewModelState.update { it.copy(editState = it.editState.copy(invalidFields = invalidFields)) }
        return invalidFields.isEmpty()
    }


}

data class SpellingUploadViewModelState(
    val state: UploadState,
    val editState: EditState<SpellingWord> = EditState(SpellingWord("", "", "")),
    val categories: List<String> = emptyList(),
    val words: List<SpellingWord> = emptyList(),
    val selectedIndex: Int? = null,
    val errorMessage: Int? = null,
    val savingResponse: List<Pair<SpellingWord, String>> = emptyList()
) {
    /**
     * Converts this [SpellingUploadViewModelState] into a more strongly typed [UploadUiState] for driving
     * the ui.
     */
    fun toUiState(): UploadUiState<SpellingWord> =
        when (state) {
            UploadState.LOADING ->
                UploadUiState.Loading

            UploadState.VIEWING -> {
                if (words.isEmpty())
                    UploadUiState.NoWords
                else
                    UploadUiState.HasWords(words = words)
            }

            UploadState.EDITING ->
                UploadUiState.Editing(
                    editState = editState,
                    categories = categories,
                )

            UploadState.SAVED ->
                UploadUiState.Saved(
                    words = words,
                    savingResult = savingResponse
                )

            UploadState.SAVING ->
                UploadUiState.Saving

            UploadState.LOAD_ERROR ->
                UploadUiState.LoadingError(
                    errorMessage = errorMessage,
                )

            UploadState.SAVING_ERROR ->
                UploadUiState.SavingError(
                    words = words,
                    errorMessage = errorMessage
                )
        }
}