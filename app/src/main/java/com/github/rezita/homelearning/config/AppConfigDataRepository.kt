package com.github.rezita.homelearning.config

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AppConfigDataRepository(
    private val dataStore: DataStore<AppConfig>,
    val defaultSpreadsheetId: String,
    val defaultScriptId: String,
) {
    val data: Flow<AppConfig> =
        dataStore.data
            .catch { e ->
                if (e is IOException) {
                    emit(AppConfig.getDefaultInstance())
                } else {
                    throw e
                }
            }

    val spreadsheetId: Flow<String> =
        data
            .map { config ->
                config.googleSpreadsheetId
                    .ifBlank { defaultSpreadsheetId }
            }

    val scriptId: Flow<String> =
        data
            .map { config -> config.googleScriptId.ifBlank { defaultScriptId } }

    /**
     * Lazy persist of BuildConfig defaults: if either proto value is blank,
     * fill it with the corresponding default and save. Called from
     * HomeViewModel on first load (Home is the entry point).
     */
    suspend fun initialise() {
        dataStore.updateData { current ->
            val spreadsheetId =
                current.googleSpreadsheetId.ifBlank { defaultSpreadsheetId }

            val scriptId =
                current.googleScriptId.ifBlank { defaultScriptId }

            current.toBuilder()
                .setGoogleSpreadsheetId(spreadsheetId)
                .setGoogleScriptId(scriptId)
                .build()
        }
    }

    suspend fun saveConfig(spreadsheetId: String, scriptId: String) {
        require(spreadsheetId.isNotBlank() && scriptId.isNotBlank()) {
            "Spreadsheet and script IDs must not be blank"
        }
        dataStore.updateData {
            it.toBuilder()
                .setGoogleSpreadsheetId(spreadsheetId)
                .setGoogleScriptId(scriptId)
                .build()
        }
    }

    suspend fun restoreDefaults() {
        saveConfig(defaultSpreadsheetId, defaultScriptId)
    }
}