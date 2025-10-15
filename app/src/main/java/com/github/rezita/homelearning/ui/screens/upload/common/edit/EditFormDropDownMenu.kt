package com.github.rezita.homelearning.ui.screens.upload.common.edit

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFormDropDownMenu(
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
        EditFormTextField(
            modifier = Modifier.menuAnchor(
                type = MenuAnchorType.PrimaryNotEditable, enabled = true
            ),
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
private fun EditFormDropDownMenuPreview() {
    val categories = listOf("home", "school")
    HomeLearningTheme {
        Scaffold {
            EditFormDropDownMenu(
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