package com.github.rezita.homelearning.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.rezita.homelearning.adapters.ReadingAdapter
import com.github.rezita.homelearning.databinding.ActivityReadingBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.uiState.UIState
import com.github.rezita.homelearning.ui.viewmodels.ReadingViewModel
import kotlinx.coroutines.launch

class ReadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadingBinding
    private val viewModel: ReadingViewModel by viewModels { ReadingViewModel.Factory }
    private var sheetAction: SheetAction = SheetAction.READ_READING_WORDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSheetAction()
        setupToolbar()
        setupView()
        setupAdapter()
    }

    private fun setupSheetAction() {
        val action: String = intent.getStringExtra("action") ?: SheetAction.READ_READING_WORDS.value
        sheetAction = SheetAction.forValue(action)!!
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.readingUIState.collect { value ->
                binding.readingProgressbar.root.visibility =
                    if (value.state == UIState.LOADING) View.VISIBLE else View.GONE
                binding.readingErrorMsg.text = value.getInfoMessage
                binding.readingErrorMsg.visibility =
                    if (value.state == UIState.ERROR) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupAdapter() {
        if (sheetAction == SheetAction.READ_READING_WORDS) {
            viewModel.getReadingWords()
        } else {
            viewModel.getCEWWords()
        }
        val adapter = ReadingAdapter()
        binding.readingWordsContainer.adapter = adapter
        lifecycleScope.launch {
            viewModel.readingUIState.collect { value ->
                adapter.loadWords(value.words)
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }
}