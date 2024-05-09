package com.github.rezita.homelearning.ui.screens.reading

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun ReadingTopAppBar(
    state: ReadingUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    callback: (Boolean) -> Unit,
    isColorDisplay: Boolean,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        titleText = when (state) {
            is ReadingUiState.Loading -> stringResource(id = R.string.app_bar_loading_title)
            is ReadingUiState.LoadingError -> stringResource(id = R.string.app_bar_error_title)
            is ReadingUiState.Downloaded -> stringResource(id = R.string.activity_reading_title)
        },
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            if (state is ReadingUiState.Downloaded) {
                // Change how to display the words (simple black / colorful)
                if (isColorDisplay) {
                    IconButton(onClick = { callback(false) }) {
                        Icon(
                            painterResource(id = R.drawable.ic_menu_black),
                            contentDescription = stringResource(id = R.string.reading_black_display),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    IconButton(onClick = { callback(true) }) {
                        Icon(
                            painterResource(id = R.drawable.ic_menu_colors),
                            contentDescription = stringResource(id = R.string.reading_colour_display),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        },
        modifier = modifier
    )
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReadingTopAppBarBlackPreview_success() {
    HomeLearningTheme {
        ReadingTopAppBar(
            state = ReadingUiState.Downloaded(emptyList()),
            canNavigateBack = true,
            navigateUp = {},
            callback = {},
            isColorDisplay = false
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReadingTopAppBarColorPreview_loading() {
    HomeLearningTheme {
        ReadingTopAppBar(
            state = ReadingUiState.Loading,
            canNavigateBack = false,
            navigateUp = {},
            callback = {},
            isColorDisplay = true
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReadingTopAppBarColorPreview_error() {
    HomeLearningTheme {
        ReadingTopAppBar(
            state = ReadingUiState.LoadingError(12),
            canNavigateBack = false,
            navigateUp = {},
            callback = {},
            isColorDisplay = true
        )
    }
}