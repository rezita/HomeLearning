package com.github.rezita.homelearning.ui.common

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.util.buttonWithImageAndDescription
import com.github.rezita.homelearning.ui.util.withRole
import org.junit.Rule
import org.junit.Test

class LearningAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    /** Learning App Bar test
     *  Only the title is set, no navBack or actions/
     * */
    @SmallTest
    @Test
    fun appBar_no_actions_no_navBack_test() {
        val title = "App Bar"
        composeTestRule.setContent {
            HomeLearningTheme {
                LearningAppBar(title = title, canNavigateBack = false, navigateUp = {}) {
                }
            }
        }
        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(0)
        //no navBack
        composeTestRule
            .onNode(
                withRole(Role.Button)
                        and
                        hasContentDescription("Back"),
                useUnmergedTree = false
            ).assertDoesNotExist()
    }

    /** Learning App Bar test
     * navBack, no actions
     * */
    @SmallTest
    @Test
    fun appBar_no_actions_navBack_test() {
        val title = "App Bar"
        composeTestRule.setContent {
            HomeLearningTheme {
                LearningAppBar(title = title, canNavigateBack = true, navigateUp = {}) {
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("tree")

        //Title
        composeTestRule.onNodeWithText(text = title).assertIsDisplayed()
        //NavBack button exists
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertExists()
        //ActionButtons - no more buttons (aka only one button is on the screen and it is the na)
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(1)

    }

    /** Learning App Bar test
     * navBack, one action
     * */
    @SmallTest
    @Test
    fun appBar_with_one_action_with_navBack_test() {
        val title = "App Bar"
        val iconDescrId = R.string.spelling_save
        val iconDescrText = context.getText(iconDescrId).toString()
        composeTestRule.setContent {
            HomeLearningTheme {
                LearningAppBar(title = title, canNavigateBack = true, navigateUp = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                painterResource(id = R.drawable.ic_save_result),
                                contentDescription = stringResource(id = iconDescrId),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                        }
                    }
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("tree")

        //Title
        composeTestRule.onNodeWithText(text = title).assertIsDisplayed()
        //There are two buttons: navBack and actionButton
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(2)

        //NavBack button exists
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //ActionButtons
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(iconDescrText),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /** Learning App Bar test
     * navBack, two actions
     * */
    @SmallTest
    @Test
    fun appBar_with_two_actions_with_navBack_test() {
        val title = "App Bar"
        val iconDescrId1 = R.string.spelling_save
        val iconDescrText1 = context.getText(iconDescrId1).toString()
        val iconDescrId2 = R.string.spelling_add_new_word
        val iconDescrText2 = context.getText(iconDescrId2).toString()
        composeTestRule.setContent {
            HomeLearningTheme {
                LearningAppBar(title = title, canNavigateBack = true, navigateUp = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                painterResource(id = R.drawable.ic_save_result),
                                contentDescription = stringResource(id = iconDescrId1),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                        }
                        IconButton(onClick = { }) {
                            Icon(
                                painterResource(id = R.drawable.ic_menu_add),
                                contentDescription = stringResource(id = iconDescrId2),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("tree")

        //Title
        composeTestRule.onNodeWithText(text = title).assertIsDisplayed()
        //There are two buttons: navBack and actionButton
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(3)

        //NavBack button exists
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //ActionButtons
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(iconDescrText1),
                useUnmergedTree = true
            ).assertIsDisplayed()

        composeTestRule
            .onNode(
                buttonWithImageAndDescription(iconDescrText2),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /** Learning App Bar test
     * no navBack, one action
     * */
    @SmallTest
    @Test
    fun appBar_with_one_action_no_navBack_test() {
        val title = "App Bar"
        val iconDescrId = R.string.spelling_save
        val iconDescrText = context.getText(iconDescrId).toString()
        composeTestRule.setContent {
            HomeLearningTheme {
                LearningAppBar(title = title, canNavigateBack = false, navigateUp = {},
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                painterResource(id = R.drawable.ic_save_result),
                                contentDescription = stringResource(id = iconDescrId),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                        }
                    }
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("tree")

        //Title
        composeTestRule.onNodeWithText(text = title).assertIsDisplayed()
        //There are two buttons: navBack and actionButton
        composeTestRule
            .onAllNodes(withRole(Role.Button))
            .assertCountEquals(1)

        //NavBack button doesn't exist
        composeTestRule
            .onNode(
                withRole(Role.Button)
                        and
                        hasContentDescription("Back"),
                useUnmergedTree = false
            ).assertDoesNotExist()
        //action button
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(iconDescrText),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }
}

