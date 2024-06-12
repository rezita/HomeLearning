package com.github.rezita.homelearning.ui.home

import android.content.Context
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.home.TabButton
import com.github.rezita.homelearning.ui.screens.home.TabWithButtons
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.util.properButton
import com.github.rezita.homelearning.ui.util.withRole
import org.junit.Rule
import org.junit.Test


class TabWithButtonsTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    /**
     * Descr: Two buttons
     * Exp. result: The two buttons displayed correctly
     */
    @Test
    fun tabWithButtons_init_without_selected_tab_test() {
        val button1TitleId = R.string.spelling_title
        val button1Title = context.getString(button1TitleId)

        val button2TitleId = R.string.reading_cew
        val button2Title = context.getString(button2TitleId)
        composeTestRule.setContent {
            val button1 = TabButton(
                titleId = button1TitleId,
                onClick = {}
            )
            val button2 = TabButton(
                titleId = button2TitleId,
                onClick = {}
            )
            val buttons = listOf(button1, button2)
            HomeLearningTheme {
                TabWithButtons(buttons = buttons)
            }
        }
        //There are 2 buttons
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(2)

        //button1
        composeTestRule
            .onNode(properButton(button1Title))
            .assertIsDisplayed()
        //button2
        composeTestRule
            .onNode(properButton(button2Title))
            .assertIsDisplayed()
    }

    /**
     * Descr: Empty list - no buttons
     * Exp. result:
     */
    @Test
    fun tabWithButtons_no_buttons_test() {
        composeTestRule.setContent {
            HomeLearningTheme {
                TabWithButtons(buttons = emptyList())
            }
        }
        //there is not any buttons
        composeTestRule
            .onNode(withRole(Role.Button))
            .assertDoesNotExist()

    }
}