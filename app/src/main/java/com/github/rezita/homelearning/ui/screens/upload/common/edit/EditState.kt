package com.github.rezita.homelearning.ui.screens.upload.common.edit

import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.Uploadable

data class EditState<T : Uploadable>(
    val word: T,
    val invalidFields: List<Pair<String, Int>> = emptyList()
) {
    fun getErrorFor(fieldkey: String) = invalidFields.find { it.first == fieldkey }?.second

    companion object {
        //Validation Spelling Word
        val INPUT_WORD = "WORD" to R.string.upload_error_word_message
        val INPUT_CATEGORY = "INPUT_CATEGORY" to R.string.upload_error_category_message

        //Validation Spanish word
        val INPUT_WORD_EN = "WORD_EN" to R.string.upload_error_word_message
        val INPUT_WORD_SP = "WORD_SP" to R.string.upload_error_word_message

        //Validate both
        val INPUT_COMMENT = "INPUT_COMMENT" to R.string.upload_error_comment_message
    }
}