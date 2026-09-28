package com.github.rezita.homelearning

import android.app.Application
import com.github.rezita.homelearning.config.AppConfigDataRepository
import com.github.rezita.homelearning.config.appConfigDataStore
import com.github.rezita.homelearning.data.AppContainer
import com.github.rezita.homelearning.data.DefaultAppContainer
import com.github.rezita.homelearning.tts.HLTextToSpeech

class HomeLearningApplication : Application() {
    lateinit var container: AppContainer
    lateinit var appConfigRepository: AppConfigDataRepository
        private set
    lateinit var textToSpeech: HLTextToSpeech

    override fun onCreate() {
        super.onCreate()
        appConfigRepository = AppConfigDataRepository(
            dataStore = appConfigDataStore,
            defaultSpreadsheetId = BuildConfig.sheetID,
            defaultScriptId = BuildConfig.scriptID
        )
        container = DefaultAppContainer(appConfigRepository)
        textToSpeech = HLTextToSpeech(applicationContext)
    }
}