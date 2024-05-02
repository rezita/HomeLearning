package com.github.rezita.homelearning.ui.screens.uploadwords.edit

import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord

data class EditState(
    val word: SpellingWord = SpellingWord("", "", ""),
    val invalidFields: List<Pair<String, Int>> = emptyList()
) {
    fun getCategoryError() = invalidFields.find { it.first == INPUT_CATEGORY.first }?.second
    fun getWordError() = invalidFields.find { it.first == INPUT_WORD.first }?.second
    fun getCommentError() = invalidFields.find { it.first == INPUT_COMMENT.first }?.second

    companion object {
        //Validation
        val INPUT_WORD = "WORD" to R.string.upload_error_word_message
        val INPUT_COMMENT = "INPUT_COMMENT" to R.string.upload_error_comment_message
        val INPUT_CATEGORY = "INPUT_CATEGORY" to R.string.upload_error_category_message
    }
}

