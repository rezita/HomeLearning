package com.github.rezita.homelearning.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.homeLearningSnackbar
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditFormTextField
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun TabWithSettings(
    uiState: SettingsUiState,
    onUserEvent: (SettingsUserEvent) -> Unit,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState.state) {
            SettingsState.LOADING, SettingsState.SAVING -> {
                LoadingProgressBar()
            }

            SettingsState.LOAD_ERROR -> {
                Text(text = stringResource(id = R.string.settings_load_failed))
                Button(
                    onClick = { onUserEvent(SettingsUserEvent.OnLoad) },
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
                ) {
                    Text(stringResource(id = R.string.error_button_caption))
                }
            }

            SettingsState.LOADED, SettingsState.SAVING_ERROR -> {
                val enabled = uiState.isEditable

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
                ) {
                    if (uiState.state == SettingsState.SAVING_ERROR) {
                        Text(text = stringResource(id = R.string.settings_save_failed))
                    }
                    
                    EditFormTextFieldWithCopyPasteIcons(
                        value = uiState.scriptId,
                        onValueChange = { onUserEvent(SettingsUserEvent.OnScriptIdChange(it)) },
                        labelId = R.string.settings_script_id,
                        enabled = enabled,
                        snackBarHostState = snackBarHostState,
                        scope = scope
                    )

                    EditFormTextFieldWithCopyPasteIcons(
                        value = uiState.sheetId,
                        onValueChange = { onUserEvent(SettingsUserEvent.OnSheetIdChange(it)) },
                        labelId = R.string.settings_sheet_id,
                        enabled = enabled,
                        snackBarHostState = snackBarHostState,
                        scope = scope
                    )

                    //saving and restore buttons
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { onUserEvent(SettingsUserEvent.OnSave) },
                            enabled = enabled && uiState.canSave,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .weight(1f)
                        ) {
                            Text(
                                stringResource(id = R.string.upload_save)
                            )
                        }
                        OutlinedButton(
                            onClick = { onUserEvent(SettingsUserEvent.OnRestore) },
                            enabled = enabled && uiState.canRestore,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .weight(1f)
                        ) {
                            Text(
                                stringResource(id = R.string.settings_restore_defaults)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditFormTextFieldWithCopyPasteIcons(
    value: String,
    onValueChange: (String) -> Unit,
    labelId: Int,
    enabled: Boolean,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        EditFormTextField(
            value = value,
            onValueChange = onValueChange,
            labelId = labelId,
            readOnly = !enabled,
            singleLine = false,
            minLines = 2,
            maxLines = 5,
        )
        CopyPasteIcons(
            copyValue = value,
            isEditable = enabled,
            onPasteEvent = onValueChange,
            snackBarHostState = snackBarHostState,
            scope = scope
        )
    }
}

@Composable
private fun CopyPasteIcons(
    copyValue: String,
    isEditable: Boolean,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onPasteEvent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val copiedMessage = stringResource(id = R.string.settings_copied)
    val pastedMessage = stringResource(id = R.string.settings_pasted)
    val emptyClipboardMessage = stringResource(id = R.string.settings_clipboard_empty)

    fun copyToClipboard(text: String) {
        clipboardManager.setText(AnnotatedString(text))
        homeLearningSnackbar(
            scope = scope,
            snackbarHostState = snackBarHostState,
            message = copiedMessage
        )
    }

    fun pasteFromClipboard(onValueChange: (String) -> Unit) {
        val clipText = clipboardManager.getText()?.text?.trim().orEmpty()
        if (clipText.isNotBlank()) {
            onValueChange(clipText)
            homeLearningSnackbar(
                scope = scope,
                snackbarHostState = snackBarHostState,
                message = pastedMessage
            )
        } else {
            homeLearningSnackbar(
                scope = scope,
                snackbarHostState = snackBarHostState,
                message = emptyClipboardMessage
            )
        }
    }

    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        IconButton(
            onClick = { copyToClipboard(copyValue) },
            enabled = copyValue.isNotEmpty()
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = stringResource(id = R.string.settings_copy)
            )
        }
        IconButton(
            onClick = {
                pasteFromClipboard { onPasteEvent(it) }
            },
            enabled = isEditable
        ) {
            Icon(
                imageVector = Icons.Default.ContentPaste,
                contentDescription = stringResource(id = R.string.settings_paste)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, apiLevel = 34)
fun SettingsTabPreview() {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    HomeLearningTheme {
        TabWithSettings(
            uiState = SettingsUiState(
                state = SettingsState.LOADED,
                scriptId = "script123",
                sheetId = "sheet123",
                savedScriptId = "script123",
                savedSheetId = "old-sheet",
                defaultScriptId = "default-script",
                defaultSheetId = "default-sheet"
            ),
            onUserEvent = {},
            snackBarHostState = snackBarHostState,
            scope = scope
        )
    }
}
