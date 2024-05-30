package com.github.rezita.homelearning.ui.screens.spelling

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.theme.spelling_correct
import com.github.rezita.homelearning.ui.theme.spelling_incorrect

const val DISABLED_ALPHA = 0.38f

enum class RadioButtonContentType {
    BUTTONS_SECOND_LINE, BUTTONS_ONLY, BUTTONS_AND_SHORT, BUTTONS_AND_LONG
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
            disabledSelectedColor = spelling_correct.copy(alpha = DISABLED_ALPHA),
            disabledUnselectedColor = spelling_correct.copy(alpha = DISABLED_ALPHA)
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
                    RadioButtonContentType.BUTTONS_ONLY, RadioButtonContentType.BUTTONS_SECOND_LINE ->
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
    val alpha = if (isEnabled) 1f else DISABLED_ALPHA
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
            colors = rbStatus.colors
        )
        Text(
            text = stringResource(id = rbStatus.shortName),
            modifier = Modifier.alpha(alpha)
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
    val alpha = if (isEnabled) 1f else DISABLED_ALPHA
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
            colors = rbStatus.colors
        )
        Text(
            text = stringResource(id = rbStatus.longName),
            modifier = Modifier.alpha(alpha)
        )
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioButtonsUnchecked(
    @PreviewParameter(RadioButtonTypeProvider::class) rbType: RadioButtonContentType
) {
    HomeLearningTheme {
        Surface {
            SpellingRadioGroup(
                selected = WordStatus.UNCHECKED,
                setSelected = {},
                rbContentType = rbType,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioButtonsSelected(
    @PreviewParameter(RadioButtonTypeProvider::class) rbType: RadioButtonContentType
) {
    HomeLearningTheme {
        Surface {
            SpellingRadioGroup(
                selected = WordStatus.CORRECT,
                setSelected = {},
                rbContentType = rbType,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingRadioButtonsDisabled(
    @PreviewParameter(RadioButtonTypeProvider::class) rbType: RadioButtonContentType
) {
    HomeLearningTheme {
        Surface {
            SpellingRadioGroup(
                selected = WordStatus.INCORRECT,
                setSelected = {},
                rbContentType = rbType,
                isEnabled = false
            )
        }
    }
}