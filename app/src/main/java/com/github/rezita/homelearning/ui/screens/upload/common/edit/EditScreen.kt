package com.github.rezita.homelearning.ui.screens.upload.common.edit

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun EditScreen(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    saveCallback: () -> Unit = {},
    cancelCallback: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_extra_big)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
        EditWordButtons(
            saveCallback = saveCallback,
            cancelCallback = cancelCallback
        )
    }
}

@Composable
fun EditWordButtons(
    saveCallback: () -> Unit,
    cancelCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = saveCallback,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
                .weight(1f)
        ) {
            Text(
                stringResource(id = R.string.upload_save)
            )
        }
        OutlinedButton(
            onClick = cancelCallback,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
                .weight(1f)
        ) {
            Text(
                stringResource(id = R.string.upload_cancel),
            )
        }

    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun EditScreenPreview() {
    val spelling1 = SpellingWord(
        word = "appear", category = "school", comment = "Y3Y4", status = WordStatus.CORRECT
    )

    val categories = listOf("home", "school")
    HomeLearningTheme {
        Scaffold {
            EditScreen(
                content = {
                    Text("This is the content")
                },
                modifier = Modifier.padding(it),
                saveCallback = {},
                cancelCallback = {},
            )
        }
    }
}
