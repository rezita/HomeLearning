package com.github.rezita.homelearning.ui.screens.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.uiState.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningAppBar(
    titleText: String = "",
    navigateUp: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        title = {
            Text(text = titleText)
        },

        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "Navigation icon",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingTopAppBar(
    status: UIState,
    callback: (Boolean) -> Unit,
    isColorDisplay: Boolean,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        titleText = when (status) {
            UIState.SUCCESS -> stringResource(id = R.string.activity_reading_title)
            UIState.LOADING -> stringResource(id = R.string.app_bar_loading_title)
            UIState.ERROR -> stringResource(id = R.string.app_bar_error_title)
            else -> ""
        },

        navigateUp = {},

        actions = {
            // Change how to display the words (simple black / colorful)
            if (isColorDisplay) {
                IconButton(onClick = { callback(false) }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_invert_colors_off_24),
                        contentDescription = stringResource(id = R.string.reading_black_display),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } else {
                IconButton(onClick = { callback(true) }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_invert_colors_24),
                        contentDescription = stringResource(id = R.string.reading_colour_display),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        modifier = modifier
    )
}


@Preview
@Composable
fun ReadingTopAppBarBlackPreview_success() {
    HomeLearningTheme {
        ReadingTopAppBar(status = UIState.SUCCESS, callback = {}, isColorDisplay = false)
    }
}

@Preview
@Composable
fun ReadingTopAppBarColorPreview_loading() {
    HomeLearningTheme {
        ReadingTopAppBar(status = UIState.LOADING, callback = {}, isColorDisplay = true)
    }
}

@Preview
@Composable
fun ReadingTopAppBarColorPreview_error() {
    HomeLearningTheme {
        ReadingTopAppBar(status = UIState.ERROR, callback = {}, isColorDisplay = true)
    }
}

@Preview
@Composable
fun ReadingTopAppBarBlackPreview_inrelevant_uistatus() {
    HomeLearningTheme {
        ReadingTopAppBar(status = UIState.EDITING, callback = {}, isColorDisplay = false)
    }
}