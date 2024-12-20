package com.github.rezita.homelearning.ui.screens.spelling

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.ui.size.HomeLearningWidthClass
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.viewmodels.AppViewModelProvider
import com.github.rezita.homelearning.ui.viewmodels.SpellingViewModel

@Composable
fun SpellingRoute(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    addNewCallback: () -> Unit,
    windowSize: HomeLearningWindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: SpellingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val spellingUiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val rbContentType = getRadioButtonType(windowSize.widthClassType1)

    SpellingScreen(
        state = spellingUiState,
        rbContentType = rbContentType,
        scope = scope,
        snackBarHostState = snackBarHostState,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
        addNewCallback = addNewCallback,
        onUserEvent = viewModel::onUserEvent,
        modifier = modifier,
    )
}

@Composable
internal fun getRadioButtonType(windowSize: HomeLearningWidthClass): RadioButtonContentType {
    val fontScale = LocalDensity.current.fontScale
    return when (windowSize) {
        HomeLearningWidthClass.XSMALL -> RadioButtonContentType.BUTTONS_SECOND_LINE
        HomeLearningWidthClass.SMALL ->
            if (fontScale <= 1.3f) RadioButtonContentType.BUTTONS_ONLY
            else RadioButtonContentType.BUTTONS_SECOND_LINE

        HomeLearningWidthClass.MEDIUM ->
            if (fontScale <= 1.3f) RadioButtonContentType.BUTTONS_AND_SHORT
            else if (fontScale <= 1.8f) RadioButtonContentType.BUTTONS_ONLY
            else RadioButtonContentType.BUTTONS_SECOND_LINE

        else ->
            if (fontScale < 1.3f) RadioButtonContentType.BUTTONS_AND_LONG
            else if (fontScale <= 1.5f) RadioButtonContentType.BUTTONS_AND_SHORT
            else RadioButtonContentType.BUTTONS_ONLY
    }
}

