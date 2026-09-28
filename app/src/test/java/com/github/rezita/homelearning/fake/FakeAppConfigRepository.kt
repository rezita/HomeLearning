package com.github.rezita.homelearning.fake

import androidx.datastore.core.DataStoreFactory
import com.github.rezita.homelearning.config.AppConfigDataRepository
import com.github.rezita.homelearning.config.AppConfigSerializer
import kotlinx.coroutines.CoroutineScope
import java.io.File

/**
 * Creates a real [AppConfigDataRepository] backed by a proto DataStore
 * stored in [configFile], for use in unit tests.
 *
 * Pass a fresh (non-existent) file per test and the [scope] of the
 * surrounding [runTest][kotlinx.coroutines.test.runTest] (e.g. `backgroundScope`),
 * so the DataStore is cancelled when the test finishes.
 */
object FakeAppConfigRepository {
    fun create(
        scope: CoroutineScope,
        configFile: File,
        spreadsheetId: String = "fake-spreadsheet-id",
        scriptId: String = "fake-script-id",
    ): AppConfigDataRepository {
        val dataStore = DataStoreFactory.create(
            serializer = AppConfigSerializer,
            scope = scope,
            produceFile = { configFile }
        )
        return AppConfigDataRepository(
            dataStore = dataStore,
            defaultSpreadsheetId = spreadsheetId,
            defaultScriptId = scriptId
        )
    }
}
