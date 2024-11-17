package com.github.rezita.homelearning

import android.app.Application
import com.github.rezita.homelearning.data.AppContainer
import com.github.rezita.homelearning.data.DefaultAppContainer
import com.github.rezita.homelearning.tts.HLTextToSpeech

class HomeLearningApplication : Application() {
    lateinit var container: AppContainer
    lateinit var textToSpeech: HLTextToSpeech

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        textToSpeech = HLTextToSpeech(applicationContext)
    }
}