package com.github.rezita.homelearning.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.github.rezita.homelearning.databinding.ActivitySpellingBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.spelling.SpellingScreen
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.viewmodels.SpellingViewModel

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


        binding = ActivitySpellingBinding.inflate(layoutInflater).apply {
            spellingComposeView.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    HomeLearningTheme {
                        SpellingScreen(viewModel = viewModel)
                    }
                }
            }
        }
        setContentView(binding.root)
    }

    private fun setupSheetAction() {
        val action: String =
            intent.getStringExtra("action") ?: SheetAction.READ_ERIK_SPELLING_WORDS.value
        sheetAction = SheetAction.forValue(action)!!
    }
}