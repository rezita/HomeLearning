package com.github.rezita.homelearning.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import arrow.core.Either
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.AddSpellingWordBinding
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.network.WordsProvider
import com.github.rezita.homelearning.utils.JSONSerializer
import com.github.rezita.homelearning.utils.RemoteError

class DialogNewSpellingWord(
    private val wordsProvider: WordsProvider,
    val sheetAction: SheetAction,
    val wordSaver: (SpellingWord) -> Unit
) : DialogFragment() {
    private lateinit var binding: AddSpellingWordBinding
    private lateinit var categorySpinner: AutoCompleteTextView

    private var spinnerAdapter: ArrayAdapter<String>? = null
    private var categories: ArrayList<String> = ArrayList()
    private var word = SpellingWord("", "", "")

    private val _maxWordLength = 30
    private val _maxCommentLength = 25
    private val _wordPattern = Regex("^[a-zA-Z][a-zA-Z\\s'-]{1,35}")
    private val _commentPattern = Regex("[\\w\\s-']{1,35}")

    constructor(
        wordsProvider: WordsProvider,
        sheetAction: SheetAction,
        wordSaver: (SpellingWord) -> Unit,
        categories: ArrayList<String>
    ) : this(wordsProvider, sheetAction, wordSaver) {
        this.categories = categories
    }

    constructor(
        wordsProvider: WordsProvider,
        sheetAction: SheetAction,
        wordSaver: (SpellingWord) -> Unit,
        word: SpellingWord,
        categories: ArrayList<String>
    ) : this(wordsProvider, sheetAction, wordSaver) {
        this.word = word
        this.categories = categories
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = AddSpellingWordBinding.inflate(layoutInflater)

            if (categories.isEmpty()) {
                loadCategories()
            } else {
                setPrBarVisibility(false)
                prepareSpinner()
            }

            setLayout()
            setTextLengthFilters()

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.upload_dialog_save, null
                )
                .setNegativeButton(R.string.upload_dialog_cancel, null)
                .setCancelable(false)
                .setTitle(R.string.save_spelling_word_title)
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()
        val dialog = dialog as AlertDialog
        val positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveBtn.setOnClickListener { saveIfValid() }

        val negativeBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeBtn.setOnClickListener { dismiss() }
    }

    private fun getWord(): SpellingWord {
        val word = binding.uploadDialogWordText.text.toString().trim()
        val comment = binding.uploadDialogCommentText.text.toString().trim()
        return SpellingWord(word, getSelectedCategory(), comment)
    }

    private fun getSelectedCategory(): String {
        val spinner = binding.uploadDialogCategoriesTextLayout.editText as AutoCompleteTextView
        return spinner.text.toString()
    }

    private fun loadCategories() {
        setPrBarVisibility(true)

        wordsProvider.loadSpellingCategories(sheetAction) { response -> onCategoriesReceived(response) }
    }

    private fun onCategoriesReceived(response: String) {
        parseItems(response).fold(
            { e -> Log.e("error", e.message) },
            { resp -> prepareSpinnerAfterLoading(resp) }
        )
    }

    private fun parseItems(jsonResponses: String): Either<RemoteError, ArrayList<String>> {
        return JSONSerializer().parseCategories(jsonResponses)
    }

    private fun prepareSpinnerAfterLoading(categoriesResponse: ArrayList<String>) {
        this.categories = categoriesResponse
        prepareSpinner()
        setPrBarVisibility(false)
    }

    private fun prepareSpinner() {
        categorySpinner = binding.uploadDialogCategoriesTextLayout.editText as AutoCompleteTextView
        if (spinnerAdapter == null) {
            spinnerAdapter = activity?.applicationContext?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    this.categories
                )
            }
                .also { adapter -> adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            categorySpinner.setAdapter(spinnerAdapter)
        }
        setSpinnerSelection()
    }

    private fun saveIfValid() {
        if (isValidInputs()) {
            wordSaver(getWord())
            dismiss()
        }
    }

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

    private fun isValidText(text: String, pattern: Regex): Boolean {
        return pattern.matches(text)
    }

    private fun setPrBarVisibility(isVisible: Boolean) {
        when (isVisible) {
            true -> binding.uploadDialogProgressbar.root.visibility = View.VISIBLE
            false -> binding.uploadDialogProgressbar.root.visibility = View.GONE
        }
    }

    private fun setLayout() {
        binding.uploadDialogCommentText.setText(word.comment)
        binding.uploadDialogWordText.setText(word.word)
    }

    private fun setSpinnerSelection() {
        if (word.category != "") {
            val spinner = binding.uploadDialogCategoriesTextLayout.editText as AutoCompleteTextView
            val position = getCategoryIndex(word.category)
            spinner.setText(spinner.adapter.getItem(position).toString(), false)
        }
    }

    private fun getCategoryIndex(category: String): Int {
        return categories.indexOf(category)
    }

    private fun getMaxLength(source: EditText, maxLength: Int): Int {
        val maxOrigin =
            source.filters?.filterIsInstance<InputFilter.LengthFilter>()?.firstOrNull()?.max
        return maxOrigin ?: maxLength
    }

    private fun setTextLengthFilters() {
        setLengthFilter(binding.uploadDialogWordText, _maxWordLength)
        setLengthFilter(binding.uploadDialogCommentText, _maxCommentLength)
    }

    private fun setLengthFilter(source: EditText, maxLength: Int) {
        val maxLengthOrig = getMaxLength(source, maxLength)
        source.filters = arrayOf(maxLengthOrig.let { InputFilter.LengthFilter(it) })
    }
}
