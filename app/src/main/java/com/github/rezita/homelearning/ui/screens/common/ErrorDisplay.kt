package com.github.rezita.homelearning.ui.screens.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun ErrorDisplayInColumn(message: String, callback: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_extra_big)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium),
            Alignment.CenterVertically
        )
    ) {
        TextWithButton(
            message = message,
            callback = callback,
        )
    }
}

@Composable
fun ErrorDisplayWithIcon(message: String, iconSource: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_extra_big)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium),
            Alignment.CenterVertically
        )
    ) {
        TextWithIcon(
            message = message,
            iconSource = iconSource
        )
    }
}


@Composable
fun ErrorDisplayInRow(message: String, callback: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
    )
    {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            TextWithButton(
                message = message,
                callback = callback,
                textModifier = Modifier
                    .weight(1f)
            )
        }
        HorizontalDivider(
            color = Color.LightGray,
            modifier = Modifier
                .height(1.dp)
                .fillMaxHeight()
                .fillMaxWidth()
        )
    }
}

@Composable
fun ErrorDisplayWithContent(
    message: String,
    callback: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium)
        )
    ) {
        ErrorDisplayInRow(
            message = message,
            callback = callback,
        )
        content()
    }
}

@Composable
private fun TextWithButton(
    message: String,
    callback: () -> Unit,
    textModifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier
) {
    Text(text = message, modifier = textModifier)
    OutlinedButton(onClick = callback, modifier = buttonModifier) {
        Text(stringResource(id = R.string.error_button_caption))
    }
}

@Composable
private fun TextWithIcon(
    message: String,
    iconSource: Int,
    textModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier
) {
    Text(text = message, modifier = textModifier)
    Icon(
        painter = painterResource(id = iconSource), contentDescription = null,
        modifier = iconModifier.size(64.dp)
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ErrorDisplayPreviewColumn() {
    HomeLearningTheme {
        Scaffold {
            ErrorDisplayInColumn(
                message = "This is a long long long long long long long long long error message",
                callback = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ErrorDisplayPreviewRow() {
    HomeLearningTheme {
        Scaffold {
            ErrorDisplayInRow(
                message = "This is a long long long long long long long long long long the error message",
                callback = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}