package com.github.rezita.homelearning.ui.reading

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.device.DeviceInteraction.Companion.setScreenOrientation
import androidx.test.espresso.device.EspressoDevice.Companion.onDevice
import androidx.test.espresso.device.action.ScreenOrientation
import androidx.test.espresso.device.rules.ScreenOrientationRule
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.reading.ReadingTopAppBar
import com.github.rezita.homelearning.ui.screens.reading.ReadingUiState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.util.buttonWithImageAndDescription
import com.github.rezita.homelearning.ui.util.withRole
import org.junit.Rule
import org.junit.Test

class ReadingTopAppBarLandscapeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**Starting state is Landscape mode*/
    @get:Rule
    val screenOrientationRule: ScreenOrientationRule =
        ScreenOrientationRule(ScreenOrientation.LANDSCAPE)

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    val actionBlackIconDescr = context.getText(R.string.reading_black_display).toString()
    val actionColorIconDescr = context.getText(R.string.reading_colour_display).toString()
    val title = "Reading"

    /**Loading state in Landscape mode
     * Back button shows, title: Reading, no action button shows*/
    @SmallTest
    @Test
    fun readingAppBar_loading_state_test() {
        val state = ReadingUiState.Loading
        val canNavigateBack = false
        onDevice().setScreenOrientation(ScreenOrientation.LANDSCAPE)
        composeTestRule.setContent {
            HomeLearningTheme {
                ReadingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    colorDisplayCallback = { },
                    isColorDisplay = false
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(0)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = false
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionBlackIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()

        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionColorIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading state in Portrait mode, isColorDisplay flase
     * Back button shows, title: Reading, no action button shows*/
    @SmallTest
    @Test
    fun readingAppBar_downloaded_state_landscape_monochrome_test() {
        val state = ReadingUiState.Downloaded(emptyList())
        val canNavigateBack = true
        onDevice().setScreenOrientation(ScreenOrientation.LANDSCAPE)

        composeTestRule.setContent {
            var isColorDisplay by remember { mutableStateOf(false) }
            HomeLearningTheme {
                ReadingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    colorDisplayCallback = { isColorDisplay = !isColorDisplay },
                    isColorDisplay = isColorDisplay
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = false).printToLog("tree")
        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(2)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionBlackIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()

        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionColorIconDescr),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**Loading state in Portrait mode, isColorDisplay flase
     * Back button shows, title: Reading, no action button shows*/
    @SmallTest
    @Test
    fun readingAppBar_downloaded_state_landscape_color_display_test() {
        val state = ReadingUiState.Downloaded(emptyList())
        val canNavigateBack = true
        onDevice().setScreenOrientation(ScreenOrientation.LANDSCAPE)

        composeTestRule.setContent {
            var isColorDisplay by remember { mutableStateOf(true) }
            HomeLearningTheme {
                ReadingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    colorDisplayCallback = { isColorDisplay = !isColorDisplay },
                    isColorDisplay = isColorDisplay
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = false).printToLog("tree")
        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(2)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionBlackIconDescr),
                useUnmergedTree = true
            ).assertIsDisplayed()

        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionColorIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading state in Portrait mode, changing the colorDisplay value by clicking on the action button
     * Back button shows, title: Reading, no action button shows*/
    @SmallTest
    @Test
    fun readingAppBar_downloaded_state_landscape_color_to_monochrome_display_test() {
        val state = ReadingUiState.Downloaded(emptyList())
        val canNavigateBack = true
        onDevice().setScreenOrientation(ScreenOrientation.LANDSCAPE)

        composeTestRule.setContent {
            var isColorDisplay by remember { mutableStateOf(true) }
            HomeLearningTheme {
                ReadingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    colorDisplayCallback = { isColorDisplay = !isColorDisplay },
                    isColorDisplay = isColorDisplay
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = false).printToLog("tree")
        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(2)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionColorIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()

        //Click
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionBlackIconDescr),
                useUnmergedTree = true
            ).assertIsDisplayed().performClick()

        //Action Icon changes
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionColorIconDescr),
                useUnmergedTree = true
            ).assertIsDisplayed()

        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionBlackIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading state in Portrait mode, changing the colorDisplay value by clicking on the action button
     * Back button shows, title: Reading, no action button shows*/
    @SmallTest
    @Test
    fun readingAppBar_downloaded_state_landscape_monochrome_to_color_test() {
        val state = ReadingUiState.Downloaded(emptyList())
        val canNavigateBack = true
        onDevice().setScreenOrientation(ScreenOrientation.LANDSCAPE)

        composeTestRule.setContent {
            var isColorDisplay by remember { mutableStateOf(false) }
            HomeLearningTheme {
                ReadingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    colorDisplayCallback = { isColorDisplay = !isColorDisplay },
                    isColorDisplay = isColorDisplay
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = false).printToLog("tree")
        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(2)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionBlackIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()

        //Click
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionColorIconDescr),
                useUnmergedTree = true
            ).assertIsDisplayed().performClick()

        //Action Icon changes
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionColorIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()

        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionBlackIconDescr),
                useUnmergedTree = true
            ).assertIsDisplayed()

    }


    /**Error state in Landscape mode
     * Back button shows, title: Reading, no action button shows*/
    @SmallTest
    @Test
    fun readingAppBar_error_state_test() {
        val state = ReadingUiState.LoadingError(R.string.loading_fail_text)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                ReadingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    colorDisplayCallback = { },
                    isColorDisplay = false
                )
            }
        }

        //composeTestRule.onRoot(useUnmergedTree = false).printToLog("tree")
        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionBlackIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()

        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionColorIconDescr),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }
}