package com.github.rezita.homelearning.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.SentenceAdapter
import com.github.rezita.homelearning.databinding.ActivityFillInSentencesBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.uiState.UIState
import com.github.rezita.homelearning.ui.viewmodels.FillInSentenceViewModel
import kotlinx.coroutines.launch


class FillInSentencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFillInSentencesBinding
    private val viewModel: FillInSentenceViewModel by viewModels { FillInSentenceViewModel.Factory }
    private var sheetAction = SheetAction.READ_IRREGULAR_VERBS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillInSentencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupSheetAction()
        setupView()
        setupAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.irregular_verb_menu, menu)
        lifecycleScope.launch {
            viewModel.sentenceUIState.collect { value ->
                menu?.findItem(R.id.menu_check)?.isVisible = value.checkable
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        lifecycleScope.launch {
            viewModel.sentenceUIState.collect { value ->
                menu?.findItem(R.id.menu_check)?.isVisible = value.checkable
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_check -> {
                checkAndUploadResults()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkAndUploadResults() {
        if (!isAllAnswered()) {
            showNotAllAnsweredDialog()
        } else {
            hideKeyboard()
            if(sheetAction == SheetAction.READ_IRREGULAR_VERBS){
                viewModel.saveIrregularVerbs()
            }else {
                viewModel.saveHomophones()
            }
        }
    }

    private fun isAllAnswered(): Boolean {
        return viewModel.sentenceUIState.value.checkable
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupSheetAction() {
        val action: String =
            intent.getStringExtra("action") ?: SheetAction.READ_IRREGULAR_VERBS.value
        sheetAction = SheetAction.forValue(action)!!
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.sentenceUIState.collect { value ->
                binding.sentencesProgressbar.root.visibility =
                    if (value.state == UIState.LOADING) View.VISIBLE else View.GONE

                binding.irregularInfoLayout.infoText.text = value.errorMessage
                binding.irregularInfoLayout.infoText.visibility =
                    if (value.errorMessage == "") View.GONE else View.VISIBLE
                val scoreText = binding.irregularInfoLayout.scoreText
                when (value.state == UIState.CHECKED) {
                    true -> {
                        scoreText.text = getScores()
                        scoreText.visibility = View.VISIBLE
                    }

                    else -> scoreText.visibility = View.GONE
                }
            }
        }
    }

    private fun setupAdapter() {
        if (sheetAction == SheetAction.READ_IRREGULAR_VERBS){
            viewModel.getIrregularVerbs()
        } else {
            viewModel.getHomophones()
        }
        val adapter = SentenceAdapter { index, value -> viewModel.updateAnswer(index, value) }
        binding.sentencesContainer.adapter = adapter
        lifecycleScope.launch {
            viewModel.sentenceUIState.collect { value ->
                adapter.loadSentences(value.sentences, value.state == UIState.CHECKED)
            }
        }
    }

    private fun getScores(): String {
        return getString(
            R.string.irregular_verb_result,
            viewModel.sentenceUIState.value.nrOfCorrect,
            viewModel.sentenceUIState.value.nrOfQuestions,
            viewModel.sentenceUIState.value.ratio
        )
    }

    private fun showNotAllAnsweredDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.irregular_verb_not_all_filled_message))
            .setNeutralButton(getString(R.string.ok)) { dialogInterface, _ -> dialogInterface.cancel() }
            .setTitle(getString(R.string.irregular_verb_dialog_title))
            .create()
            .show()
    }

    private fun hideKeyboard() {
        //edittext.onEditorAction(EditorInfo.IME_ACTION_DONE);
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }
}