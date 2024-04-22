package com.github.rezita.homelearning.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.github.rezita.homelearning.databinding.ActivityUploadSpellingWordsBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.uploadwords.UploadWordsScreen
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.viewmodels.UploadWordViewModel

class UploadSpellingWordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadSpellingWordsBinding
    private lateinit var viewModel: UploadWordViewModel

    private var sheetAction: SheetAction = SheetAction.SAVE_ERIK_WORDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSheetAction()

        viewModel =
            ViewModelProvider(
                this,
                UploadWordViewModel.UploadWordViewModelFactory(sheetAction)
            )
                .get(
                    UploadWordViewModel::class.java
                )

        binding = ActivityUploadSpellingWordsBinding.inflate(layoutInflater).apply {
            uploadComposeView.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    HomeLearningTheme {
                        UploadWordsScreen(viewModel = viewModel)
                    }
                }
            }
        }
        setContentView(binding.root)

    }

    private fun setupSheetAction() {
        val action: String = intent.getStringExtra("action") ?: SheetAction.SAVE_ERIK_WORDS.value
        sheetAction = SheetAction.forValue(action)!!
    }
}
