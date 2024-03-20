package com.github.rezita.homelearning.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.UploadWordsAdapter
import com.github.rezita.homelearning.databinding.ActivityUploadSpellingWordsBinding
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.uiState.UIState
import com.github.rezita.homelearning.ui.viewmodels.MAX_COMMENT_LENGTH
import com.github.rezita.homelearning.ui.viewmodels.MAX_WORD_LENGTH
import com.github.rezita.homelearning.ui.viewmodels.UploadWordViewModel
import kotlinx.coroutines.launch

class UploadSpellingWordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadSpellingWordsBinding
    private val viewModel: UploadWordViewModel by viewModels { UploadWordViewModel.Factory }
    private lateinit var adapter: UploadWordsAdapter
    private var sheetAction: SheetAction = SheetAction.SAVE_ERIK_WORDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadSpellingWordsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        loadCategories()
        setupToolbar()
        setupSheetAction()
        setupView()
        setupAdapter()
    }

    private fun setupSheetAction() {
        val action: String = intent.getStringExtra("action") ?: SheetAction.SAVE_ERIK_WORDS.value
        sheetAction = SheetAction.forValue(action)!!
    }

    private fun loadCategories() {
        if (sheetAction == SheetAction.SAVE_ERIK_WORDS) {
            viewModel.loadErikCategories()
        } else {
            viewModel.loadMarkCategories()
        }
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupView() {
        setTextLengthFilters()
        lifecycleScope.launch {
            viewModel.uploadWordUIState.collect { value ->
                binding.uploadWordsProgressbar.root.visibility =
                    if (value.state == UIState.LOADING) View.VISIBLE else View.GONE

                binding.uploadWordsInfoLayout.infoText.text = value.getInfoMessage

                binding.uploadDialogCategoryText.setAdapter(
                    ArrayAdapter(
                        applicationContext,
                        android.R.layout.simple_spinner_item,
                        value.categories
                    )
                )
                val invalidFields = value.currentWord.invalidFields
                if (!invalidFields.isNullOrEmpty()) {
                    val validationFields = initValidationFields()
                    invalidFields.forEach {
                        val message = it.second
                        validationFields[it.first]?.error = getString(message)
                        validationFields[it.first]?.setErrorIconDrawable(0)
                    }
                }
            }
        }
    }

    private fun setTextLengthFilters() {
        setLengthFilter(binding.uploadDialogWordText, MAX_WORD_LENGTH)
        setLengthFilter(binding.uploadDialogCommentText, MAX_COMMENT_LENGTH)
    }

    private fun setLengthFilter(source: EditText, maxLength: Int) {
        val maxLengthOrig = getMaxLength(source, maxLength)
        source.filters = arrayOf(maxLengthOrig.let { InputFilter.LengthFilter(it) })
    }

    private fun getMaxLength(source: EditText, maxLength: Int): Int {
        val maxOrigin =
            source.filters?.filterIsInstance<InputFilter.LengthFilter>()?.firstOrNull()?.max
        return maxOrigin ?: maxLength
    }

    private fun initValidationFields() = mapOf(
        UploadWordViewModel.INPUT_WORD.first to binding.uploadDialogWordTextLayout,
        UploadWordViewModel.INPUT_COMMENT.first to binding.uploadDialogCommentTextLayout,
        UploadWordViewModel.INPUT_CATEGORY.first to binding.uploadDialogCategoriesTextLayout
    )

    private fun setupAdapter() {
        adapter = UploadWordsAdapter(
            context = this,
            selectionDeleteInteraction = { index -> onSelectedItemDelete(index) },
            selectionChangeInteraction = { index, word -> onItemSelectedForEditing(index, word) },
        )
        binding.uploadWordsContainer.adapter = adapter
        lifecycleScope.launch {
            viewModel.uploadWordUIState.collect { value ->
                adapter.loadWords(value.words)
            }
        }
    }

    private fun onSelectedItemDelete(index: Int) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirm_remove_upload_word))
            .setPositiveButton(
                getString(R.string.yes_button),
                DialogInterface.OnClickListener { _, _ -> viewModel.removeWord(index) })
            .setNegativeButton(getString(R.string.no_button)) { dialogInterface, _ -> dialogInterface.cancel() }
            .create()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.spelling_menu, menu)
        lifecycleScope.launch {
            viewModel.uploadWordUIState.collect { value ->
                menu?.findItem(R.id.menu_add_new)?.isVisible = value.isExpandable
                menu?.findItem(R.id.menu_save)?.isVisible = value.isSavable
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        lifecycleScope.launch {
            viewModel.uploadWordUIState.collect { value ->
                menu?.findItem(R.id.menu_add_new)?.isVisible = value.isExpandable
                menu?.findItem(R.id.menu_save)?.isVisible = value.isSavable
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_new -> {
                createNewSpellingWord()
                true
            }

            R.id.menu_save -> {
                if (sheetAction == SheetAction.SAVE_ERIK_WORDS) {
                    viewModel.saveErikSpellingWords()
                } else {
                    viewModel.saveMarkSpellingWords()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onItemSelectedForEditing(index: Int, word: SpellingWord) {
        viewModel.setForEditing(index, word)
    }

    private fun createNewSpellingWord() {
        viewModel.setForEditing()
    }
}
