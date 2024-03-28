package com.github.rezita.homelearning.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.SentenceAdapter
import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.databinding.ActivityFillInSentencesBinding
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.viewmodels.FillInSentenceViewModel
import kotlinx.coroutines.launch


class FillInSentencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFillInSentencesBinding
    private lateinit var viewModel: FillInSentenceViewModel
    private var sheetAction = SheetAction.READ_IRREGULAR_VERBS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSheetAction()
        viewModel =
            ViewModelProvider(
                this,
                FillInSentenceViewModel.FillInSentenceViewModelFactory(sheetAction)
            )
                .get(
                    FillInSentenceViewModel::class.java
                )

        binding = ActivityFillInSentencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        setupProgressBar()
        setupScoreText()
        setupView()
        setupAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.irregular_verb_menu, menu)

        lifecycleScope.launch {
            viewModel.isAllAnswered.collect() { value ->
                menu?.findItem(R.id.menu_check)?.isVisible = value
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        lifecycleScope.launch {
            viewModel.isAllAnswered.collect { value ->
                menu?.findItem(R.id.menu_check)?.isVisible = value
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
            if (sheetAction == SheetAction.READ_IRREGULAR_VERBS) {
                viewModel.saveIrregularVerbs()
            } else {
                viewModel.saveHomophones()
            }
        }
    }

    private fun isAllAnswered(): Boolean {
        return viewModel.isAllAnswered.value
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupProgressBar() {
        lifecycleScope.launch {
            viewModel.sentenceUIState.collect { value ->
                binding.sentencesProgressbar.root.visibility =
                    when (value) {
                        is NormalRepositoryResult.Downloading -> View.VISIBLE
                        is NormalRepositoryResult.Uploading -> View.VISIBLE
                        else -> View.GONE
                    }
            }
        }
    }

    private fun setupScoreText() {
        lifecycleScope.launch {
            viewModel.sentenceUIState.collect { value ->
                val scoreText = binding.irregularInfoLayout.scoreText
                when (value) {
                    is NormalRepositoryResult.Uploaded -> {
                        scoreText.text = getScores(value.data)
                        scoreText.visibility = View.VISIBLE
                    }
                    else -> {
                        scoreText.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupSheetAction() {
        val action: String =
            intent.getStringExtra("action") ?: SheetAction.READ_IRREGULAR_VERBS.value
        sheetAction = SheetAction.forValue(action)!!
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.sentenceUIState.collect { value ->
                val infotext = binding.irregularInfoLayout.infoText
                when (value) {
                    is NormalRepositoryResult.DownloadingError -> {
                        infotext.visibility = View.VISIBLE
                        infotext.text = value.message
                    }

                    is NormalRepositoryResult.UploadError -> {
                        infotext.visibility = View.VISIBLE
                        infotext.text = value.message
                    }
                    is NormalRepositoryResult.Uploaded -> {
                        infotext.visibility = View.VISIBLE
                        infotext.text = value.message
                    }

                    else -> {
                        infotext.visibility = View.GONE
                        infotext.text = ""
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        if (sheetAction == SheetAction.READ_IRREGULAR_VERBS) {
            viewModel.getIrregularVerbs()
        } else {
            viewModel.getHomophones()
        }
        val adapter = SentenceAdapter { index, value -> viewModel.updateAnswer(index, value) }
        binding.sentencesContainer.adapter = adapter
        lifecycleScope.launch {
            viewModel.sentenceUIState.collect { value ->
                if (value is NormalRepositoryResult.Downloaded) {
                    adapter.loadSentences(value.data)
                }
                adapter.setChecked(value is NormalRepositoryResult.Uploaded)
            }
        }
    }

    private fun getScores(sentences: List<FillInSentence>): String {
        val nrOfQuestions = sentences.size
        val nrOfCorrect = sentences.filter { it.status == WordStatus.CORRECT }.size
        val ratio = if (nrOfQuestions == 0) 0 else nrOfCorrect * 100 / nrOfQuestions
        return getString(
            R.string.irregular_verb_result,
            nrOfCorrect,
            nrOfQuestions,
            ratio
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