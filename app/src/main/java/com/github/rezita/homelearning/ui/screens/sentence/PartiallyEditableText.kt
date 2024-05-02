package com.github.rezita.homelearning.ui.screens.sentence

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun PartiallyEditableText(
    prefix: String,
    suffix: String,
    value: String,      //This is the editable part
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    editableMinLength: Int = 10,    //editable min length - for the underline part
    baseTextColor: Color = Color.Black,

    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors()

) {
    val lastSelection by remember { mutableStateOf(TextRange(prefix.length + value.length)) }

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = lastSelection
            )
        )
    }
    TextField(
        value = textFieldValueState,
        onValueChange = { newTextFieldValueState ->
            textFieldValueState = newTextFieldValueState
            onValueChange(textFieldValueState.text)
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = PartiallyEditableTextVisualTransformation(
            prefix = prefix,
            suffix = suffix,
            minLength = editableMinLength,
            baseTextColor = baseTextColor
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}

class PartiallyEditableTextVisualTransformation(
    private val prefix: String,
    private val suffix: String,
    private val minLength: Int,
    private val baseTextColor: Color
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText =
            getDisplayedText(prefix, suffix, text.toString(), minLength, baseTextColor)
        return TransformedText(
            text = transformedText,
            offsetMapping = PartiallyEditableTextOffsetMapping(
                prefix.length,
                text.length
            )
        )
    }
}

private fun getDisplayedText(
    firstPart: String,
    lastPart: String,
    editedText: String,
    minLength: Int,
    baseTextColor: Color
): AnnotatedString {
    val editablePart = getDisplayableEditableText(editedText, minLength)
    return buildAnnotatedString {
        val str = firstPart + editablePart + lastPart
        append(str)
        addStyle(
            style = SpanStyle(textDecoration = TextDecoration.Underline),
            start = firstPart.length,
            end = firstPart.length + editablePart.length
        )
        addStyle(
            style = SpanStyle(color = baseTextColor),
            start = 0,
            end = firstPart.length
        )
        addStyle(
            style = SpanStyle(color = baseTextColor),
            start = firstPart.length + editablePart.length,
            end = str.length
        )
    }
}

private fun getDisplayableEditableText(editableText: String, minLength: Int): String {
    val lengthDiff = minLength - editableText.length
    if (lengthDiff > 0) {
        return editableText.extendWithSpace(lengthDiff)
    }
    return editableText
}

fun String.extendWithSpace(nrOfSpaces: Int): String {
    return this + "\u00A0".repeat(nrOfSpaces)
}


class PartiallyEditableTextOffsetMapping(
    private val prefixLength: Int,
    private val textLength: Int
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        if (offset <= 0) return prefixLength
        // @returns -> cursor position for the transformed text
        return offset + prefixLength
    }

    override fun transformedToOriginal(offset: Int): Int {
        //prefix
        if (offset <= prefixLength) {
            return 0
        }
        //in the text
        if (offset < prefixLength + textLength) {
            return offset - (prefixLength + 1)
        }
        //spaces and suffix
        return textLength
    }
}

@Preview(showBackground = true)
@Composable
fun PartiallyTextPreview2_wo_value() {
    HomeLearningTheme {
        PartiallyEditableText(
            prefix = "Hel",
            suffix = "lo",
            value = "",
            onValueChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PartiallyTextPreview2_with_value() {
    HomeLearningTheme {
        PartiallyEditableText(
            prefix = "Hel",
            suffix = "lo",
            value = "llloo",
            onValueChange = {},
            colors = TextFieldDefaults.colors()
                .copy(
                    focusedTextColor = Color.Blue,
                    unfocusedTextColor = Color.Blue,

                    )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PartiallyTextPreview2_with_value_begin() {
    HomeLearningTheme {
        PartiallyEditableText(
            prefix = "Helllo",
            suffix = "",
            value = "before",
            onValueChange = {},
            baseTextColor = Color.Red,
            colors = TextFieldDefaults.colors()
                .copy(
                    focusedTextColor = Color.Blue,
                    unfocusedTextColor = Color.Blue,
                ),
        )
    }
}