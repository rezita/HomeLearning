package com.github.rezita.homelearning.ui.screens.upload.spanishupload.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.ui.screens.upload.common.MAX_COMMENT_LENGTH
import com.github.rezita.homelearning.ui.screens.upload.common.MAX_WORD_LENGTH_SP
import com.github.rezita.homelearning.ui.screens.upload.common.SpanishUploadUserEvent
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditFormTextField
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditState

@Composable
fun SpanishWordEditForm(
    state: UploadUiState.Editing<SpanishWord>,
    onUserEvent: (SpanishUploadUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        EditFormTextField(
            value = state.editState.word.wordEn,
            onValueChange = { onUserEvent(SpanishUploadUserEvent.OnWordEnChangeForEditedWord(it)) },
            labelId = R.string.upload_spanish_word_en_label,
            error = state.editState.getErrorFor(EditState.Companion.INPUT_WORD_EN.first),
            maxLength = MAX_WORD_LENGTH_SP
        )
        EditFormTextField(
            value = state.editState.word.wordSp,
            onValueChange = { onUserEvent(SpanishUploadUserEvent.OnWordSpChangeForEditedWord(it)) },
            labelId = R.string.upload_spanish_word_sp_label,
            error = state.editState.getErrorFor(EditState.Companion.INPUT_WORD_EN.first),
            maxLength = MAX_WORD_LENGTH_SP
        )
        EditFormTextField(
            value = state.editState.word.comment,
            onValueChange = { onUserEvent(SpanishUploadUserEvent.OnCommentChangeForEditedWord(it)) },
            labelId = R.string.upload_comment_label,
            error = state.editState.getErrorFor(EditState.Companion.INPUT_COMMENT.first),
            maxLength = MAX_COMMENT_LENGTH
        )
    }
}