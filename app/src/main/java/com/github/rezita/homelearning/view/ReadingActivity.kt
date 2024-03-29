package com.github.rezita.homelearning.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.github.rezita.homelearning.databinding.ActivityReadingBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.reading.ReadingScreen
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.viewmodels.ReadingViewModel

class ReadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadingBinding
    private lateinit var viewModel: ReadingViewModel
    private var sheetAction: SheetAction = SheetAction.READ_READING_WORDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSheetAction()

        viewModel =
            ViewModelProvider(
                this,
                ReadingViewModel.ReadingWordViewModelFactory(sheetAction)
            )
                .get(
                    ReadingViewModel::class.java
                )
        binding = ActivityReadingBinding.inflate(layoutInflater).apply {
            readingComposeView.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    HomeLearningTheme {
                        ReadingScreen(viewModel)
                    }
                }
            }
        }
        setContentView(binding.root)
    }


    private fun setupSheetAction() {
        val action: String = intent.getStringExtra("action") ?: SheetAction.READ_READING_WORDS.value
        sheetAction = SheetAction.forValue(action)!!
    }
}