package com.github.rezita.homelearning.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rezita.homelearning.config.AppConfigDataRepository
import com.github.rezita.homelearning.ui.screens.home.SettingsState
import com.github.rezita.homelearning.ui.screens.home.SettingsUiState
import com.github.rezita.homelearning.ui.screens.home.SettingsUserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val appConfigDataRepository: AppConfigDataRepository) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        SettingsUiState(state = SettingsState.LOADING)
    )

    val uiState: StateFlow<SettingsUiState> =
        viewModelState.map { it }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                SettingsUiState(SettingsState.LOADING)
            )

    init {
        load()
    }

    fun onUserEvent(event: SettingsUserEvent) {
        when (event) {
            SettingsUserEvent.OnLoad -> load()
            SettingsUserEvent.OnRestore -> restoreValues()
            SettingsUserEvent.OnSave -> saveValues()
            is SettingsUserEvent.OnScriptIdChange -> setScriptId(event.value)
            is SettingsUserEvent.OnSheetIdChange -> setSheetId(event.value)
        }
    }

    fun load() {
        viewModelScope.launch {
            viewModelState.update { it.copy(state = SettingsState.LOADING) }
            try {
                // Lazy persist of BuildConfig defaults on first entry (Home is entry point)
                appConfigDataRepository.initialise()
                val sheetId = appConfigDataRepository.spreadsheetId.first()
                val scriptId = appConfigDataRepository.scriptId.first()
                viewModelState.update {
                    it.copy(
                        state = SettingsState.LOADED,
                        scriptId = scriptId,
                        sheetId = sheetId,
                        savedScriptId = scriptId,
                        savedSheetId = sheetId,
                        defaultScriptId = appConfigDataRepository.defaultScriptId,
                        defaultSheetId = appConfigDataRepository.defaultSpreadsheetId
                    )
                }
            } catch (e: Exception) {
                viewModelState.update { it.copy(state = SettingsState.LOAD_ERROR) }
            }
        }
    }

    fun setSheetId(id: String) {
        viewModelState.update { it.copy(sheetId = id) }
    }

    fun setScriptId(id: String) {
        viewModelState.update { it.copy(scriptId = id) }
    }

    fun saveValues() {
        val current = viewModelState.value
        if (!current.canSave) return
        viewModelScope.launch {
            viewModelState.update { it.copy(state = SettingsState.SAVING) }
            try {
                appConfigDataRepository.saveConfig(
                    spreadsheetId = current.sheetId,
                    scriptId = current.scriptId
                )
                viewModelState.update {
                    it.copy(
                        state = SettingsState.LOADED,
                        savedSheetId = current.sheetId,
                        savedScriptId = current.scriptId
                    )
                }
            } catch (e: Exception) {
                viewModelState.update { it.copy(state = SettingsState.SAVING_ERROR) }
            }
        }
    }

    fun restoreValues() {
        val current = viewModelState.value
        if (!current.canRestore) return
        viewModelScope.launch {
            viewModelState.update { it.copy(state = SettingsState.SAVING) }
            try {
                appConfigDataRepository.restoreDefaults()
                val sheetId = appConfigDataRepository.spreadsheetId.first()
                val scriptId = appConfigDataRepository.scriptId.first()
                viewModelState.update {
                    it.copy(
                        state = SettingsState.LOADED,
                        scriptId = scriptId,
                        sheetId = sheetId,
                        savedScriptId = scriptId,
                        savedSheetId = sheetId
                    )
                }
            } catch (e: Exception) {
                viewModelState.update { it.copy(state = SettingsState.SAVING_ERROR) }
            }
        }
    }
}