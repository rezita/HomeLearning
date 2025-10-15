package com.github.rezita.homelearning.ui.screens.common.upload.edit

import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.Uploadable

data class EditState<T: Uploadable>(
    val word: T,
    val invalidFields: List<Pair<String, Int>> = emptyList()
) {
    fun getErrorFor(fieldkey: String) = invalidFields.find { it.first == fieldkey }?.second

    companion object {
        //Validation
        val INPUT_WORD = "WORD" to R.string.upload_error_word_message
        val INPUT_COMMENT = "INPUT_COMMENT" to R.string.upload_error_comment_message
        val INPUT_CATEGORY = "INPUT_CATEGORY" to R.string.upload_error_category_message
    }
}