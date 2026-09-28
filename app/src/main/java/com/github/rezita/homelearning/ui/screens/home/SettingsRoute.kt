package com.github.rezita.homelearning.ui.screens.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.rezita.homelearning.ui.viewmodels.AppViewModelProvider
import com.github.rezita.homelearning.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingsRoute(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    TabWithSettings(
        uiState = uiState,
        onUserEvent = viewModel::onUserEvent,
        scope = scope,
        snackBarHostState = snackBarHostState,
        modifier = modifier
    )
}
