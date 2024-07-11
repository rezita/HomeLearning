package com.github.rezita.homelearning.ui.screens.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},

    ) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            Text(text = title)
        },

        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = actions
    )
}

@Composable
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun AppBarPreview(
    @PreviewParameter(BooleanParameterProvider::class) canNavigateBack: Boolean
) {
    HomeLearningTheme {

        LearningAppBar(
            title = "Top App Bar",
            canNavigateBack = canNavigateBack,
            navigateUp = {},
        )
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun AppBarPreviewWithActions(
    @PreviewParameter(BooleanParameterProvider::class) canNavigateBack: Boolean
) {
    HomeLearningTheme {
        LearningAppBar(
            title = "Top App Bar",
            canNavigateBack = canNavigateBack,
            navigateUp = {},
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_result),
                        contentDescription = stringResource(id = R.string.spelling_save),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                }
                IconButton(onClick = { }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_add),
                        contentDescription = stringResource(id = R.string.spelling_add_new_word),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                }
            },
        )
    }
}