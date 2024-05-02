package com.github.rezita.homelearning

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.github.rezita.homelearning.ui.HomeLearningApp
import com.github.rezita.homelearning.ui.rememberHomeLearningAppState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme


class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HomeLearningTheme {
                val appState = rememberHomeLearningAppState(
                    windowSizeClass = calculateWindowSizeClass(activity = this),
                    wordRepository = (application as HomeLearningApplication).container.wordRepository
                )
                HomeLearningApp(appState)
            }
        }
    }

}