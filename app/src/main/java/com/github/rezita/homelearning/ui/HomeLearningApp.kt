package com.github.rezita.homelearning.ui

import androidx.compose.runtime.Composable
import com.github.rezita.homelearning.navigation.HomeLearningNavigation
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun HomeLearningApp(
    appState: HomeLearningAppState
) {
    HomeLearningTheme {
        HomeLearningNavigation(
            homeLearningAppState = appState,
        )

    }
}