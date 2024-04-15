package com.github.rezita.homelearning.ui.screens.spelling

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

val SpellingRadioItems = listOf(
    Pair(R.string.erik_spelling_correct, WordStatus.CORRECT),
    Pair(R.string.erik_spelling_incorrect, WordStatus.INCORRECT)
)

@Composable
fun SpellingRadioGroup(
    selected: WordStatus,
    setSelected: (selected: WordStatus) -> Unit,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        SpellingRadioItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == item.second,
                    onClick = {
                        setSelected(item.second)
                    },
                    enabled = isEnabled
                )
                Text(
                    text = stringResource(id = item.first),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioNoSelectionPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.UNCHECKED,
                setSelected = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioCorrectPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.CORRECT,
                setSelected = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}
