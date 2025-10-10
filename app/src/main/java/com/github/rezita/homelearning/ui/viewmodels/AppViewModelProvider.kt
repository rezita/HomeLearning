package com.github.rezita.homelearning.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.rezita.homelearning.HomeLearningApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for FillInSentenceViewModel
        initializer {
            FillInSentenceViewModel(
                this.createSavedStateHandle(),
                homeLearningApplication().container.wordRepository
            )
        }

        // Initializer for SpellingViewModel
        initializer {
            SpellingViewModel(
                this.createSavedStateHandle(),
                homeLearningApplication().container.wordRepository,
                homeLearningApplication().textToSpeech
            )
        }
        // Initializer for ReadingViewModel
        initializer {
            ReadingViewModel(
                this.createSavedStateHandle(),
                homeLearningApplication().container.wordRepository
            )
        }

        // Initializer for SpanishViewModel
        initializer {
            SpanishViewModel(
                this.createSavedStateHandle(),
                homeLearningApplication().container.wordRepository,
                homeLearningApplication().textToSpeech
            )
        }

        // Initializer for UploadWordViewModel
        initializer {
            UploadWordViewModel(
                this.createSavedStateHandle(),
                homeLearningApplication().container.wordRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [HomeLearningApplication].
 */
fun CreationExtras.homeLearningApplication(): HomeLearningApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HomeLearningApplication)
