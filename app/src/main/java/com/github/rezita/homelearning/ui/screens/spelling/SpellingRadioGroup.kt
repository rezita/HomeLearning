package com.github.rezita.homelearning.ui.screens.spelling

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
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
import com.github.rezita.homelearning.ui.theme.spelling_correct
import com.github.rezita.homelearning.ui.theme.spelling_incorrect

enum class RadioButtonContentType {
    BUTTONS_ONLY, BUTTONS_AND_SHORT, BUTTONS_AND_LONG
}

data class RadioButtonStatus(
    val status: WordStatus,
    @StringRes val longName: Int,
    @StringRes val shortName: Int,
    val colors: RadioButtonColors
)


val SpellingRadioItems = listOf(
    RadioButtonStatus(
        status = WordStatus.CORRECT,
        longName = R.string.spelling_correct,
        shortName = R.string.spelling_correct_short,
        colors = RadioButtonColors(
            selectedColor = spelling_correct,
            unselectedColor = spelling_correct,
            disabledSelectedColor = spelling_correct.copy(alpha = 0.38f),
            disabledUnselectedColor = spelling_correct.copy(alpha = 0.38f)
        )
    ),
    RadioButtonStatus(
        status = WordStatus.INCORRECT,
        longName = R.string.spelling_incorrect,
        shortName = R.string.spelling_incorrect_short,
        colors = RadioButtonColors(
            selectedColor = spelling_incorrect,
            unselectedColor = spelling_incorrect,
            disabledSelectedColor = spelling_incorrect.copy(alpha = 0.38f),
            disabledUnselectedColor = spelling_incorrect.copy(alpha = 0.38f)
        )
    )
)

@Composable
fun SpellingRadioGroup(
    selected: WordStatus,
    setSelected: (selected: WordStatus) -> Unit,
    rbContentType: RadioButtonContentType,
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
                when (rbContentType) {
                    RadioButtonContentType.BUTTONS_ONLY ->
                        SpellingRadioButtonOnly(
                            rbStatus = item,
                            isSelected = item.status == selected,
                            setSelected = setSelected,
                            isEnabled = isEnabled
                        )

                    RadioButtonContentType.BUTTONS_AND_SHORT ->
                        SpellingRadioButtonWithShort(
                            rbStatus = item,
                            isSelected = item.status == selected,
                            setSelected = setSelected,
                            isEnabled = isEnabled
                        )

                    RadioButtonContentType.BUTTONS_AND_LONG ->
                        SpellingRadioButtonWithLong(
                            rbStatus = item,
                            isSelected = item.status == selected,
                            setSelected = setSelected,
                            isEnabled = isEnabled
                        )
                }
            }
        }
    }
}

@Composable
fun SpellingRadioButtonOnly(
    rbStatus: RadioButtonStatus,
    isSelected: Boolean,
    setSelected: (selected: WordStatus) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    RadioButton(
        modifier = modifier,
        selected = isSelected,
        onClick = {
            setSelected(rbStatus.status)
        },
        enabled = isEnabled,
        colors = rbStatus.colors
    )

}

@Composable
fun SpellingRadioButtonWithShort(
    rbStatus: RadioButtonStatus,
    isSelected: Boolean,
    setSelected: (selected: WordStatus) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                setSelected(rbStatus.status)
            },
            enabled = isEnabled,
            //colors = rbStatus.colors
        )
        Text(
            text = stringResource(id = rbStatus.shortName),
        )
    }
}

@Composable
fun SpellingRadioButtonWithLong(
    rbStatus: RadioButtonStatus,
    isSelected: Boolean,
    setSelected: (selected: WordStatus) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                setSelected(rbStatus.status)
            },
            enabled = isEnabled,
            //colors = rbStatus.colors
        )
        Text(
            text = stringResource(id = rbStatus.longName),
        )
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioNoSelectionButtonsOnlyPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.UNCHECKED,
                setSelected = {},
                rbContentType = RadioButtonContentType.BUTTONS_ONLY,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioNoSelectionButtonsAndShortPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.UNCHECKED,
                setSelected = {},
                rbContentType = RadioButtonContentType.BUTTONS_AND_SHORT,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioNoSelectionButtonsAndLongPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.UNCHECKED,
                setSelected = {},
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioCorrectButtonsOnlyPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.CORRECT,
                setSelected = {},
                rbContentType = RadioButtonContentType.BUTTONS_ONLY,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioCorrectLongPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.CORRECT,
                setSelected = {},
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioCorrectButtonsDisabledPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.CORRECT,
                setSelected = {},
                rbContentType = RadioButtonContentType.BUTTONS_ONLY,
                isEnabled = false,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioCorrectLongDisabledPreview() {
    HomeLearningTheme {
        Scaffold {
            SpellingRadioGroup(
                selected = WordStatus.CORRECT,
                setSelected = {},
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                isEnabled = false,
                modifier = Modifier.padding(it)
            )
        }
    }
}
