package com.github.rezita.homelearning.ui.screens.reading

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
    colorDisplayCallback: (Boolean) -> Unit,
    redoCallback: () -> Unit,
    isColorDisplay: Boolean,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        title = stringResource(id = R.string.activity_reading_title),
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,

        actions = {
            if (state is ReadingUiState.Downloaded && LocalConfiguration.current.orientation != Configuration.ORIENTATION_PORTRAIT) {
                // Change how to display the words (simple black / colorful)
                if (isColorDisplay) {
                    IconButton(onClick = { colorDisplayCallback(false) }) {
                        Icon(
                            painterResource(id = R.drawable.ic_menu_black),
                            contentDescription = stringResource(id = R.string.reading_black_display),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    IconButton(onClick = { colorDisplayCallback(true) }) {
                        Icon(
                            painterResource(id = R.drawable.ic_menu_colors),
                            contentDescription = stringResource(id = R.string.reading_colour_display),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                //reload icon
                IconButton(onClick = { redoCallback() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_refresh),
                        contentDescription = stringResource(id = R.string.menu_reload),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        modifier = modifier
    )
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReadingTopAppBarBlackPreview_downloaded() {
    HomeLearningTheme {
        ReadingTopAppBar(
            state = ReadingUiState.Downloaded(emptyList()),
            canNavigateBack = true,
            navigateUp = {},
            colorDisplayCallback = {},
            redoCallback = {},
            isColorDisplay = false
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReadingTopAppBarColorPreview_loading() {
    HomeLearningTheme {
        ReadingTopAppBar(
            state = ReadingUiState.Loading,
            canNavigateBack = false,
            navigateUp = {},
            colorDisplayCallback = {},
            redoCallback = {},
            isColorDisplay = true
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReadingTopAppBarColorPreview_error() {
    HomeLearningTheme {
        ReadingTopAppBar(
            state = ReadingUiState.LoadingError(12),
            canNavigateBack = false,
            navigateUp = {},
            colorDisplayCallback = {},
            redoCallback = {},
            isColorDisplay = true
        )
    }
}