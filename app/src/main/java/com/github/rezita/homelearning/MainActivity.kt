package com.github.rezita.homelearning

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalDensity
import androidx.window.layout.WindowMetricsCalculator
import com.github.rezita.homelearning.ui.HomeLearningApp
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateHomeLearningWindowSizeClass()
            HomeLearningApp(windowSizeClass)
        }

    }

    @Composable
    fun calculateHomeLearningWindowSizeClass(): HomeLearningWindowSizeClass {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)
        val density = LocalDensity.current
        val size = with(density) { metrics.bounds.toComposeRect().size.toDpSize() }
        return HomeLearningWindowSizeClass.calculateFromSize(size)
    }
}