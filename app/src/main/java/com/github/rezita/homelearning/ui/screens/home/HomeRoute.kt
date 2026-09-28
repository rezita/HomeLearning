package com.github.rezita.homelearning.ui.screens.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeRoute(
    tabs: List<HomeLearningTabItem>,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    selectedTab: Int = 0
) {
    HomeScreen(tabs, snackBarHostState, modifier, selectedTab)
}