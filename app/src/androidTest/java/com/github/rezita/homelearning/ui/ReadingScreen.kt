package com.github.rezita.homelearning.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.rezita.homelearning.view.ReadingActivity
import org.junit.Rule

class ReadingScreen {

    @Rule
    @JvmField
    val composeTestRule = createAndroidComposeRule<ReadingActivity>()
}