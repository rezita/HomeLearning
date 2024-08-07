package com.github.rezita.homelearning.ui

import androidx.compose.runtime.Composable
import com.github.rezita.homelearning.data.AppContainer
import com.github.rezita.homelearning.navigation.HomeLearningNavigation
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun HomeLearningApp(
    windowSizeClass: HomeLearningWindowSizeClass
) {
    HomeLearningTheme {
        HomeLearningNavigation(
            windowSizeClass = windowSizeClass
        )
    }
}