package com.github.rezita.homelearning

import android.app.Application
import com.github.rezita.homelearning.data.AppContainer
import com.github.rezita.homelearning.data.DefaultAppContainer

class HomeLearningApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}