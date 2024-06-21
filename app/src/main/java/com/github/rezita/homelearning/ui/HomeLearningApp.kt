package com.github.rezita.homelearning.ui

import androidx.compose.runtime.Composable
import com.github.rezita.homelearning.navigation.HomeLearningNavigation

@Composable
fun HomeLearningApp(
    appState: HomeLearningAppState
) {
    HomeLearningNavigation(
        homeLearningAppState = appState,
    )
}