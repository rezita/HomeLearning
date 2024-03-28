package com.github.rezita.homelearning.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.SpellingWordAdapter
import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.databinding.ActivitySpellingBinding
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.viewmodels.SpellingViewModel
import kotlinx.coroutines.launch

class SpellingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpellingBinding
    private lateinit var viewModel: SpellingViewModel
    private var sheetAction: SheetAction = SheetAction.READ_ERIK_SPELLING_WORDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSheetAction()
        viewModel =
            ViewModelProvider(
                this,
                SpellingViewModel.SpellingViewModelFactory(sheetAction)
            )
                .get(
                    SpellingViewModel::class.java
                )


        binding = ActivitySpellingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupProgressBar()
        setupScoreText()
        setupInfoText()
        setupAdapter()
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupProgressBar() {
        lifecycleScope.launch {
            viewModel.spellingUIState.collect { value ->
                binding.spellingProgressbar.root.visibility =
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
            viewModel.spellingUIState.collect { value ->
                val scoreText = binding.spellingInfoLayout.scoreText
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

    private fun setupInfoText() {
        lifecycleScope.launch {
            viewModel.spellingUIState.collect { value ->
                val infotext = binding.spellingInfoLayout.infoText
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

    @SuppressLint("StringFormatInvalid", "StringFormatMatches")
    fun getScores(words: List<SpellingWord>): String {
        val nrOfQuestions = words.size
        val nrOfCorrect = words.filter { it.status == WordStatus.CORRECT }.size
        val ratio = if (nrOfQuestions == 0) 0 else nrOfCorrect * 100 / nrOfQuestions
        return getString(
            R.string.spelling_result,
            nrOfCorrect,
            nrOfQuestions,
            ratio
        )
    }

    private fun setupAdapter() {
        if (sheetAction == SheetAction.READ_ERIK_SPELLING_WORDS) {
            viewModel.getErikSpellingWords()
        } else {
            viewModel.getMarkSpellingWords()
        }
        val adapter =
            SpellingWordAdapter { index, status -> viewModel.updateWordStatus(index, status) }
        binding.spellingWordsContainer.adapter = adapter
        lifecycleScope.launch {
            viewModel.spellingUIState.collect { value ->
                if (value is NormalRepositoryResult.Downloaded) {
                    adapter.loadWords(
                        value.data
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.spelling_menu, menu)
        lifecycleScope.launch {
            viewModel.spellingUIState.collect { value ->
                menu?.findItem(R.id.menu_save)?.isVisible =
                    (value is NormalRepositoryResult.Downloaded || value is NormalRepositoryResult.UploadError)
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        lifecycleScope.launch {
            viewModel.spellingUIState.collect { value ->
                menu?.findItem(R.id.menu_save)?.isVisible =
                    (value is NormalRepositoryResult.Downloaded || value is NormalRepositoryResult.UploadError)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /*
            R.id.menu_add_new -> {
                createNewSpellingWord()
                true
            }
*/
            R.id.menu_save -> {
                if (sheetAction == SheetAction.READ_ERIK_SPELLING_WORDS) {
                    viewModel.saveErikSpellingResults()
                } else {
                    viewModel.saveMarkSpellingResults()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSheetAction() {
        val action: String =
            intent.getStringExtra("action") ?: SheetAction.READ_ERIK_SPELLING_WORDS.value
        sheetAction = SheetAction.forValue(action)!!
    }
}