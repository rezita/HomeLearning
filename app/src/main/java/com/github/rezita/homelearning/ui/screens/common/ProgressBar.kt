package com.github.rezita.homelearning.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import kotlin.random.Random

val progressBarMessages = listOf(
    "Discovering new ways of making you wait.",
    "Your time is very important to us. Please wait.",
    "Kindly hold on until I finish a cup of coffee.",
    "Don't panic, Just count to infinite.",
    "Let's talk about trains...",
    "Wash your teeth before go to bed.",
    "I'll finish when pigs fly.",
    "The lights are on, but nobody's home."
)

@Composable
fun LoadingProgressBar(modifier: Modifier = Modifier) {
    val index = Random.nextInt(progressBarMessages.size)
    val loadingMessage = progressBarMessages[index]
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = loadingMessage)
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Preview
@Composable
fun LoadingProgressBarPreview() {
    HomeLearningTheme {
        LoadingProgressBar()
    }
}