package com.github.rezita.homelearning.ui.screens.home

data class SettingsUiState(
    val state: SettingsState,
    val scriptId: String = "",
    val sheetId: String = "",
    val savedScriptId: String = "",
    val savedSheetId: String = "",
    val defaultScriptId: String = "",
    val defaultSheetId: String = ""
) {
    val isEditable: Boolean
        get() = state == SettingsState.LOADED || state == SettingsState.SAVING_ERROR

    val canSave: Boolean
        get() = isEditable &&
                scriptId.isNotBlank() &&
                sheetId.isNotBlank() &&
                (scriptId != savedScriptId || sheetId != savedSheetId)

    val canRestore: Boolean
        get() = isEditable &&
                (savedScriptId != defaultScriptId || savedSheetId != defaultSheetId)
}

enum class SettingsState{
    LOADING, LOAD_ERROR, LOADED, SAVING, SAVING_ERROR
}