package com.github.rezita.homelearning.ui.screens.reading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.viewmodels.ReadingViewModel

@Composable
fun ReadingRoute(
    viewModel: ReadingViewModel,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    windowSize: HomeLearningWindowSizeClass,
    modifier: Modifier = Modifier
) {
    val readingState by viewModel.readingUIState.collectAsState()
    var isTopAppBarShown by remember {
        mutableStateOf(true)
    }
    val configuration = LocalConfiguration.current

    ReadingScreen(
        readingState = readingState,
        windowSize = windowSize,
        orientation = configuration.orientation,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
        onLoadCallback = viewModel::load,
        isTopAppBarShown = isTopAppBarShown,
        onChangeTopAppBarVisibility = { isTopAppBarShown = !isTopAppBarShown },
        isColorDisplay = viewModel.isColourDisplay,
        colorDisplayChange = viewModel::setColorDisplay,
        modifier = modifier

    )
}