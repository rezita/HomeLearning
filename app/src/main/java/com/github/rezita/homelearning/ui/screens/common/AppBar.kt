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
import com.github.rezita.homelearning.R

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
                    painterResource(id = R.drawable.ic_navigation_back),
                    contentDescription = stringResource(id = R.string.back_button),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        actions = actions
    )
}

