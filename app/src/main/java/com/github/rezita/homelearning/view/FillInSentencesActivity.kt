package com.github.rezita.homelearning.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.github.rezita.homelearning.databinding.ActivityFillInSentencesBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.sentence.FillInSentenceSentenceScreen
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.viewmodels.FillInSentenceViewModel


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

        binding = ActivityFillInSentencesBinding.inflate(layoutInflater).apply {
            sentenceComposeView.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    HomeLearningTheme {
                        FillInSentenceSentenceScreen(viewModel = viewModel)
                    }
                }
            }
        }
        setContentView(binding.root)
    }

    private fun setupSheetAction() {
        val action: String =
            intent.getStringExtra("action") ?: SheetAction.READ_IRREGULAR_VERBS.value
        sheetAction = SheetAction.forValue(action)!!
    }
/*
        private fun hideKeyboard() {
            //edittext.onEditorAction(EditorInfo.IME_ACTION_DONE);
            try {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                // TODO: handle exception
            }
        }
     */
}