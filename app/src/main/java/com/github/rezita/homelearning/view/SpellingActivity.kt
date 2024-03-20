package com.github.rezita.homelearning.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.SpellingWordAdapter
import com.github.rezita.homelearning.databinding.ActivitySpellingBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.uiState.UIState
import com.github.rezita.homelearning.ui.viewmodels.SpellingViewModel
import kotlinx.coroutines.launch

class SpellingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpellingBinding
    private val viewModel: SpellingViewModel by viewModels { SpellingViewModel.Factory }
    private var sheetAction: SheetAction = SheetAction.READ_ERIK_SPELLING_WORDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySpellingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupSheetAction()
        setupView()
        setupAdapter()
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.spellingUIState.collect { value ->
                binding.spellingProgressbar.root.visibility =
                    if (value.state == UIState.LOADING) View.VISIBLE else View.GONE

                val infoText = binding.spellingInfoLayout.infoText
                infoText.text = value.message
                infoText.visibility = when (value.message) {
                    "" -> View.GONE
                    else -> View.VISIBLE
                }

                val scoreText = binding.spellingInfoLayout.scoreText
                scoreText.text = getScores()
                scoreText.visibility = when (value.state) {
                    UIState.SUCCESS -> View.VISIBLE
                    else -> View.GONE
                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid", "StringFormatMatches")
    fun getScores(): String {
        return getString(
            R.string.spelling_result,
            viewModel.spellingUIState.value.correct,
            viewModel.spellingUIState.value.answered,
            viewModel.spellingUIState.value.ratio
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
                adapter.loadWords(value.words)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.spelling_menu, menu)
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