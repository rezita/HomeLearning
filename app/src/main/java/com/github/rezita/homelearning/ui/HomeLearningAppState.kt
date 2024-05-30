package com.github.rezita.homelearning.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass

class HomeLearningAppState(
    val navController: NavHostController,
    val windowSizeClass: HomeLearningWindowSizeClass,
    private val wordRepository: WordRepository,
) {
}


@Composable
fun rememberHomeLearningAppState(
    navController: NavHostController = rememberNavController(),
    windowSizeClass: HomeLearningWindowSizeClass,
    wordRepository: WordRepository,
): HomeLearningAppState {
    return remember(
        navController,
        windowSizeClass,
        wordRepository
    ) {
        HomeLearningAppState(
            navController = navController,
            windowSizeClass = windowSizeClass,
            wordRepository = wordRepository
        )
    }
}