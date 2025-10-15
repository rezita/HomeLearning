@file:Suppress("UNCHECKED_CAST")

package com.github.rezita.homelearning.ui.screens.upload.spellingupload.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.screens.upload.common.MAX_COMMENT_LENGTH
import com.github.rezita.homelearning.ui.screens.upload.common.MAX_WORD_LENGTH
import com.github.rezita.homelearning.ui.screens.upload.common.SpellingUploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditFormDropDownMenu
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditFormTextField
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditState

@Composable
fun SpellingWordEditForm(
    state: UploadUiState.Editing<SpellingWord>,
    onUserEvent: (SpellingUploadUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        EditFormTextField(
            value = state.editState.word.word,
            onValueChange = { onUserEvent(SpellingUploadUserEvent.OnWordChangeForEditedWord(it)) },
            labelId = R.string.upload_word_label,
            error = state.editState.getErrorFor(EditState.Companion.INPUT_WORD.first),
            maxLength = MAX_WORD_LENGTH
        )
        EditFormTextField(
            value = state.editState.word.comment,
            onValueChange = { onUserEvent(SpellingUploadUserEvent.OnCommentChangeForEditedWord(it)) },
            labelId = R.string.upload_comment_label,
            error = state.editState.getErrorFor(EditState.Companion.INPUT_COMMENT.first),
            maxLength = MAX_COMMENT_LENGTH
        )
        EditFormDropDownMenu(
            options = state.categories,
            labelId = R.string.upload_category_label,
            selectedItem = state.editState.word.category,
            onOptionSelected = {
                if (it != null) {
                    onUserEvent(SpellingUploadUserEvent.OnCategoryChangeForEditedWord(it))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),

            error = state.editState.getErrorFor(EditState.Companion.INPUT_CATEGORY.first)
        )
    }
}