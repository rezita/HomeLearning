package com.github.rezita.homelearning.ui.screens.home

sealed interface SettingsUserEvent {
    data object OnRestore : SettingsUserEvent
    data object OnSave : SettingsUserEvent
    data object OnLoad : SettingsUserEvent
    data class OnSheetIdChange(val value: String) : SettingsUserEvent
    data class OnScriptIdChange(val value: String) : SettingsUserEvent
}