package com.github.rezita.homelearning.ui.screens.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.theme.sentence_correct
import com.github.rezita.homelearning.ui.theme.sentence_incorrect

@Composable
fun ResultIcon(status: WordStatus, modifier: Modifier = Modifier) {
    if (status == WordStatus.CORRECT) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(id = R.string.sentences_result_correct),
            tint = sentence_correct,
            modifier = modifier
        )
    } else {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(id = R.string.sentences_result_incorrect),
            tint = sentence_incorrect,
            modifier = modifier
        )
    }
}

@Composable
fun ResultIconWithText(status: WordStatus) {
    val resultTextId =
        if (status == WordStatus.CORRECT) R.string.result_correct else R.string.result_incorrect
    val textColor = if (status == WordStatus.CORRECT) sentence_correct else sentence_incorrect

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        ResultIcon(status)
        Text(text = stringResource(resultTextId), color = textColor)

    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ResultIconWithTextPreview() {
    HomeLearningTheme {
        Column {
            ResultIconWithText(WordStatus.CORRECT)
            ResultIconWithText(WordStatus.INCORRECT)
            ResultIconWithText(WordStatus.UNCHECKED)
        }
    }
}
