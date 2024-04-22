package com.github.rezita.homelearning.ui.screens.uploadwords.edit

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.uploadwords.UploadUiState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.viewmodels.MAX_COMMENT_LENGTH
import com.github.rezita.homelearning.ui.viewmodels.MAX_WORD_LENGTH

@Composable
fun EditScreen(
    state: UploadUiState.Editing,
    saveCallback: () -> Unit,
    cancelCallback: () -> Unit,
    onWordChangeCallback: (String) -> Unit,
    onCommentChangeCallback: (String) -> Unit,
    onCategoryChangeCallback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_big)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextField(
            value = state.editState.word.word,
            onValueChange = { onWordChangeCallback(it) },
            labelId = R.string.upload_word_label,
            error = state.editState.getWordError(),
            maxLength = MAX_WORD_LENGTH
        )
        EditTextField(
            value = state.editState.word.comment,
            onValueChange = { onCommentChangeCallback(it) },
            labelId = R.string.upload_comment_label,
            error = state.editState.getCommentError(),
            maxLength = MAX_COMMENT_LENGTH
        )
        CategoryDropDownMenu(
            options = state.categories,
            labelId = R.string.upload_category_label,
            selectedItem = state.editState.word.category,
            onOptionSelected = {
                if (it != null) {
                    onCategoryChangeCallback(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
            error = state.editState.getCategoryError()
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
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
}

@Composable
private fun EditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    labelId: Int,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    error: Int? = null,
    maxLength: Int? = null
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.padding_medium)),
    ) {
        if (error != null) {
            Text(
                text = stringResource(id = error),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.padding_medium)),
                textAlign = TextAlign.Start
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (maxLength == null || it.length <= maxLength) {
                    onValueChange(it)
                }
            },
            modifier = modifier
                .fillMaxWidth(),
            readOnly = readOnly,
            label = { Text(stringResource(id = labelId)) },
            trailingIcon = trailingIcon,
            isError = error != null,
            singleLine = true,
            colors = colors
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropDownMenu(
    options: List<String>,
    labelId: Int,
    selectedItem: String,
    onOptionSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    error: Int? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(selectedItem) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        EditTextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            labelId = labelId,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(),
            error = error
        )
        ExposedDropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            border = if (error != null) BorderStroke(2.dp, MaterialTheme.colorScheme.error)
            else BorderStroke(2.dp, MaterialTheme.colorScheme.primary)

        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        selectedOptionText = selectionOption
                        expanded = false

                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun CategoryDropDownMenuPreview() {
    val categories = listOf("home", "school")
    HomeLearningTheme {
        Scaffold {
            CategoryDropDownMenu(
                options = categories,
                labelId = R.string.upload_category_label,
                selectedItem = "",
                onOptionSelected = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it),
                error = null
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpellingItemPreview() {
    val spelling1 = SpellingWord(
        word = "appear",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )

    val categories = listOf("home", "school")
    HomeLearningTheme {
        Scaffold {
            EditScreen(
                state = UploadUiState.Editing(
                    EditState(word = spelling1),
                    categories = categories,
                    isSavable = false,
                    isExpandable = false
                ),
                saveCallback = {},
                cancelCallback = {},
                onWordChangeCallback = {},
                onCommentChangeCallback = {},
                onCategoryChangeCallback = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}