package com.github.rezita.homelearning.ui.screens.reading

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithIcon
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass

@Composable
fun ReadingScreen(
    readingState: ReadingUiState,
    windowSize: HomeLearningWindowSizeClass,
    orientation: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onLoadCallback: () -> Unit,
    isTopAppBarShown: Boolean,
    onChangeTopAppBarVisibility: () -> Unit,
    isColorDisplay: Boolean,
    colorDisplayChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier

) {
    Scaffold(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = { _ ->
                    onChangeTopAppBarVisibility()
                }
            )
        },
        topBar = {
            if (isTopAppBarShown) {
                ReadingTopAppBar(
                    state = readingState,
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateUp,
                    colorDisplayCallback = colorDisplayChange,
                    redoCallback = onLoadCallback,
                    isColorDisplay = isColorDisplay
                )
            }
        }
    ) {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                ErrorDisplayWithIcon(
                    message = stringResource(id = R.string.msg_turn_landscape_mode),
                    iconSource = R.drawable.screen_rotation_24px,
                    modifier = modifier
                        .padding(it)
                )

            else ->
                ReadingContent(
                    windowSize = windowSize,
                    state = readingState,
                    isColorDisplay = isColorDisplay,
                    onLoadCallback = onLoadCallback,
                    modifier = modifier.padding(it)
                )
        }
    }
}
