package com.github.rezita.homelearning.ui.screens.reading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.viewmodels.AppViewModelProvider
import com.github.rezita.homelearning.ui.viewmodels.ReadingViewModel

@Composable
fun ReadingRoute(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    windowSize: HomeLearningWindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: ReadingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val readingState by viewModel.readingUIState.collectAsState()
    var showTopAppBar by remember { mutableStateOf(true) }
    val configuration = LocalConfiguration.current

    ReadingScreen(
        readingState = readingState,
        windowSize = windowSize,
        orientation = configuration.orientation,
        canNavigateBack = canNavigateBack,
        showTopAppBar = showTopAppBar,
        isColorDisplay = viewModel.isColourDisplay,
        navigateUp = navigateUp,
        onChangeTopAppBarVisibility = { showTopAppBar = !showTopAppBar },
        onUserEvent = viewModel::onEvent,
        modifier = modifier

    )
}