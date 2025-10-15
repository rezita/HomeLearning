package com.github.rezita.homelearning.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.navigation.SpellingUploadDestination
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.upload.common.SpanishUploadUserEvent
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

class SpanishUploadViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordRepository: WordRepository,
) : ViewModel() {
    private val sheetAction: SheetAction =
        savedStateHandle.toRoute<SpellingUploadDestination>().sheetAction

    private val viewModelState = MutableStateFlow(
        SpanishUploadViewModelState(state = UploadState.VIEWING)
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map { viewModelState.value.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun onUserEvent(event: SpanishUploadUserEvent) {
        when (event) {
            UploadUserEvent.OnSave -> saveSpanishWords()
            UploadUserEvent.OnAddNew -> setForEditing(null)
            UploadUserEvent.OnCancelEditing -> resetEditing()
            UploadUserEvent.OnSaveEditedWord -> updateWordsAfterEditing()
            is UploadUserEvent.OnRemoveWord -> removeWord(event.index)
            is UploadUserEvent.OnPrepareForEditing -> setForEditing(event.index)
            is SpanishUploadUserEvent.OnWordEnChangeForEditedWord -> updateWordEn(event.wordEn)
            is SpanishUploadUserEvent.OnWordSpChangeForEditedWord -> updateWordSp(event.wordSp)
            is SpanishUploadUserEvent.OnCommentChangeForEditedWord -> updateComment(event.comment)
        }
    }

    /**after saving and before new word will be added*/
    private fun resetViewModelState() {
        if (viewModelState.value.state == UploadState.SAVED) {
            viewModelState.update {
                it.copy(
                    state = UploadState.VIEWING,
                    editState = EditState(SpanishWord("", "", "", false)),
                    words = emptyList(),
                    selectedIndex = null,
                    errorMessage = null,
                    savingResponse = emptyList(),
                )
            }
        }
    }

    private fun resetEditing() {
        viewModelState.update {
            it.copy(
                state = UploadState.VIEWING,
                editState = EditState(SpanishWord("", "", "", false)),
                selectedIndex = null
            )
        }
    }


    private fun saveSpanishWords() {
        when (sheetAction) {
            SheetAction.SAVE_ZITA_SPANISH_WORDS -> {
                saveSpanishWordsToRepo { words -> wordRepository.saveSpanishWords(words) }
            }

            else -> viewModelState.update {
                it.copy(
                    state = UploadState.SAVING_ERROR,
                    errorMessage = R.string.msg_wrong_action
                )
            }
        }
    }

    private fun saveSpanishWordsToRepo(callback: suspend (List<SpanishWord>) -> RepositoryResult<String>) {
        viewModelState.update { it.copy(state = UploadState.LOADING) }

        viewModelScope.launch {
            val result = callback(viewModelState.value.words)
            try {
                viewModelState.update {
                    when (result) {
                        is RepositoryResult.Success -> it.copy(
                            state = UploadState.SAVED,
                            savingResponse = parseResponse(
                                result.data,
                                viewModelState.value.words
                            ),
                            errorMessage = null,
                        )

                        is RepositoryResult.Error -> it.copy(
                            state = UploadState.SAVING_ERROR,
                            errorMessage = R.string.snackBar_save_error,
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("error", e.printStackTrace().toString())
                viewModelState.update {
                    it.copy(
                        state = UploadState.SAVING_ERROR,
                        errorMessage = R.string.snackBar_save_unexpected_error,
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
            viewModelState.value.editState
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

    private fun updateWordEn(word: String) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = it.editState.word.copy(wordEn = word)))
        }
    }

    private fun updateWordSp(category: String) {
        viewModelState.update {
            it.copy(editState = it.editState.copy(word = it.editState.word.copy(wordSp = category)))
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
        if (!isValidText(wordToValidate.wordEn, wordPattern)) {
            invalidFields.add(EditState.INPUT_WORD_EN)
        }
        if (!isValidText(wordToValidate.wordEn, wordPattern)) {
            invalidFields.add(EditState.INPUT_WORD_SP)
        }
        if (wordToValidate.comment.isNotEmpty() &&
            !isValidText(wordToValidate.comment, commentPattern)
        ) {
            invalidFields.add(EditState.INPUT_COMMENT)
        }

        viewModelState.update { it.copy(editState = it.editState.copy(invalidFields = invalidFields)) }
        return invalidFields.isEmpty()
    }
}

data class SpanishUploadViewModelState(
    val state: UploadState,
    val editState: EditState<SpanishWord> = EditState(SpanishWord("", "", "", false)),
    val words: List<SpanishWord> = emptyList(),
    val selectedIndex: Int? = null,
    val errorMessage: Int? = null,
    val savingResponse: List<Pair<SpanishWord, String>> = emptyList()
) {
    fun toUiState(): UploadUiState<SpanishWord> =
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
                UploadUiState.Editing(editState = editState, emptyList())

            UploadState.SAVED ->
                UploadUiState.Saved(
                    words = words,
                    savingResult = savingResponse
                )

            UploadState.SAVING ->
                UploadUiState.Saving

            UploadState.SAVING_ERROR ->
                UploadUiState.SavingError(
                    words = words,
                    errorMessage = errorMessage
                )

            //this state is not valid in this viewmodel - > there's nothing to load
            UploadState.LOAD_ERROR ->
                UploadUiState.NoWords
        }
}
