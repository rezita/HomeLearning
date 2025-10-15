package com.github.rezita.homelearning.ui.screens.upload.common.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.ErrorText

@Composable
fun EditFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelId: Int,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    error: Int? = null,
    maxLength: Int? = null
) {
    val focusManager = LocalFocusManager.current
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.padding_medium)),
    ) {
        if (error != null) {
            ErrorText(stringResource(id = error))
        }
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (maxLength == null || it.length <= maxLength) {
                    onValueChange(it)
                }
            },
            modifier = modifier.fillMaxWidth(),
            readOnly = readOnly,
            label = { Text(stringResource(id = labelId)) },
            trailingIcon = trailingIcon,
            isError = error != null,
            singleLine = true,
            colors = colors,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move the text indicator's focus to the next field or the drop down menu
                    focusManager.moveFocus(FocusDirection.Next)
                },
            ),
        )
    }
}
