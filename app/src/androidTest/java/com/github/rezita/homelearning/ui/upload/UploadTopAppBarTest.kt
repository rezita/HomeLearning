package com.github.rezita.homelearning.ui.upload

import android.content.Context
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState
import com.github.rezita.homelearning.ui.screens.upload.common.components.UploadTopAppBar
import com.github.rezita.homelearning.ui.screens.upload.common.edit.EditState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.util.buttonWithImageAndDescription
import com.github.rezita.homelearning.ui.util.withRole
import org.junit.Rule
import org.junit.Test

class UploadTopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val title = context.getText(R.string.activity_upload_words).toString()

    private val actionSave = context.getText(R.string.upload_saving_words).toString()
    private val actionAddNew = context.getText(R.string.upload_add_new).toString()

    /**Loading state with no navBack
     * Back button doesn't show, title: Uploading Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_loading_state_test() {
        val state = UploadUiState.Loading
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
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
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading state with navBack
     * Back button shows, title: Upload Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_loading_state_with_navBack_test() {
        val state = UploadUiState.Loading
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saving state with no navBack
     * Back button doesn't show, title: Uploading Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_saving_state_test() {
        val state = UploadUiState.Saving
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
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
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saving state with navBack
     * Back button shows, title: Upload Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_saving_state_with_navBack_test() {
        val state = UploadUiState.Saving
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**NoWords state with no navBack
     * Back button doesn't show, title: Uploading Words, add new action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_noWords_state_test() {
        val state = UploadUiState.NoWords
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**NoWords state with navBack
     * Back button shows, title: Uploading Words, add new action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_noWords_state_with_navBack_test() {
        val state = UploadUiState.NoWords
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(2)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**HasWords, but not full state with no navBack
     * Back button doesn't show, title: Uploading Words, save and add new action buttons show*/
    @SmallTest
    @Test
    fun uploadTopAppBar_hasWords_butNotFull_state_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(spelling1, spelling2)
        val state = UploadUiState.HasWords(words)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(2)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertIsDisplayed()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**HasWords, but not full state with navBack
     * Back button shows, title: Uploading Words, save and add new action buttons show*/
    @SmallTest
    @Test
    fun uploadTopAppBar_hasWords_butNotFull_state_with_navBack_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(spelling1, spelling2)
        val state = UploadUiState.HasWords(words)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(3)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertIsDisplayed()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**HasWords, and full state with no navBack
     * Back button doesn't show, title: Uploading Words, save action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_hasWords_Full_state_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(
            spelling1,
            spelling2,
            spelling1,
            spelling2,
            spelling1,
            spelling2,
            spelling1,
            spelling2,
            spelling1,
            spelling2,
        )
        val state = UploadUiState.HasWords(words)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertIsDisplayed()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**HasWords, and full state with navBack
     * Back button shows, title: Uploading Words, save action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_hasWords_Full_state_with_navBack_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(
            spelling1, spelling2,
            spelling1, spelling2,
            spelling1, spelling2,
            spelling1, spelling2,
            spelling1, spelling2,
        )
        val state = UploadUiState.HasWords(words)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(2)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertIsDisplayed()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saved state with no navBack
     * Back button doesn't show, title: Uploading Words, add new action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_saved_state_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(spelling1, spelling2)
        val response = listOf(Pair(spelling1, "Success"), Pair(spelling2, "Existed"))
        val state = UploadUiState.Saved(words, response)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**Saved state with navBack
     * Back button shows, title: Upload Words, add new action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_saved_state_with_navBack_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(spelling1, spelling2)
        val response = listOf(Pair(spelling1, "Success"), Pair(spelling2, "Existed"))
        val state = UploadUiState.Saved(words, response)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(2)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**Editing state with no navBack
     * Back button doesn't show, title: Uploading Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_editing_state_test() {
        val state = UploadUiState.Editing(EditState(), listOf("home", "school"))
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
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
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Editing state with navBack
     * Back button shows, title: Upload Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_editing_state_with_navBack_test() {
        val state = UploadUiState.Editing(EditState(), listOf("home", "school"))
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading Error state with no navBack
     * Back button doesn't show, title: Uploading Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_loading_error_state_test() {
        val state = UploadUiState.LoadingError(R.string.loading_fail_text)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
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
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading error state with navBack
     * Back button shows, title: Upload Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_loading_error_state_with_navBack_test() {
        val state = UploadUiState.LoadingError(R.string.loading_fail_text)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saving Error state with no navBack
     * Back button doesn't show, title: Uploading Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_saving_error_state_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(spelling1, spelling2)
        val state = UploadUiState.SavingError(words, R.string.loading_fail_text)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
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
                useUnmergedTree = true
            )
            .assertDoesNotExist()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saving error state with navBack
     * Back button shows, title: Upload Words, no action button shows*/
    @SmallTest
    @Test
    fun uploadTopAppBar_saving_error_state_with_navBack_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.CORRECT
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.INCORRECT
            )
        val words = listOf(spelling1, spelling2)
        val state = UploadUiState.SavingError(words, R.string.loading_fail_text)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                UploadTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    onUserEvent = {}
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = title)
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            ).assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }
}