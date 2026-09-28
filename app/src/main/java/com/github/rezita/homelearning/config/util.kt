package com.github.rezita.homelearning.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore

private const val APP_CONFIG_FILE = "app_config.pb"

val Context.appConfigDataStore: DataStore<AppConfig> by dataStore(
    fileName = APP_CONFIG_FILE,
    serializer = AppConfigSerializer,
)