package com.github.rezita.homelearning.ui.spelling

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
import com.github.rezita.homelearning.ui.screens.spelling.SpellingTopAppBar
import com.github.rezita.homelearning.ui.screens.spelling.SpellingUiState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.util.buttonWithImageAndDescription
import com.github.rezita.homelearning.ui.util.withRole
import org.junit.Rule
import org.junit.Test

class SpellingTopAppBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val title = context.getText(R.string.activity_spelling).toString()
    private val actionSave = context.getText(R.string.spelling_save).toString()
    private val actionAddNew = context.getText(R.string.spelling_add_new_word).toString()
    private val actionRedo = context.getText(R.string.menu_redo).toString()

    /**Loading state with no navBack
     * Back button doesn't show, title: Spelling, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_state_test() {
        val state = SpellingUiState.Loading
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading state with navBack
     * Back button shows, title: Spelling, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_state_with_navBack_test() {
        val state = SpellingUiState.Loading
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with no navBack, empty words list
     * Back button doesn't show, title: Spelling, adding action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_empty_state_test() {
        val state = SpellingUiState.Loaded(emptyList())
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with navBack empty words list
     * Back button shows, title: Spelling, adding new word action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_empty_state_with_navBack_test() {
        val state = SpellingUiState.Loaded(emptyList())
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with no navBack, all unchecked
     * Back button doesn't show, title: Spelling, adding action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_all_unchecked_state_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.UNCHECKED
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.UNCHECKED
            )
        val words = listOf(spelling1, spelling2)
        val state = SpellingUiState.Loaded(words)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with navBack all unchecked
     * Back button shows, title: Spelling, adding new word action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_all_unchecked_state_with_navBack_test() {
        val spelling1 =
            SpellingWord(
                word = "appear",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.UNCHECKED
            )
        val spelling2 =
            SpellingWord(
                word = "behave",
                category = "school",
                comment = "Y3Y4",
                status = WordStatus.UNCHECKED
            )
        val words = listOf(spelling1, spelling2)
        val state = SpellingUiState.Loaded(words)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with no navBack, at least one not unchecked
     * Back button doesn't show, title: Spelling, saving and adding action buttons are shown*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_not_all_unchecked_state_test() {
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
                status = WordStatus.UNCHECKED
            )
        val words = listOf(spelling1, spelling2)
        val state = SpellingUiState.Loaded(words)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with navBack not all all unchecked
     * Back button shows, title: Spelling, saving and adding new word action buttons are shown*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_not_all_unchecked_state_with_navBack_test() {
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
                status = WordStatus.UNCHECKED
            )
        val words = listOf(spelling1, spelling2)
        val state = SpellingUiState.Loaded(words)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with no navBack, all answered
     * Back button doesn't show, title: Spelling, saving and adding action buttons are shown*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_not_all_answered_state_test() {
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
        val state = SpellingUiState.Loaded(words)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with navBack all answered
     * Back button shows, title: Spelling, saving and adding new word action buttons are shown*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_not_all_answered_state_with_navBack_test() {
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
        val state = SpellingUiState.Loaded(words)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading error with no navBack
     * Back button doesn't show, title: Spelling, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_error_state_test() {
        val state = SpellingUiState.LoadingError(R.string.loading_fail_text)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading error state with navBack
     * Back button shows, title: Spelling, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_error_state_with_navBack_test() {
        val state = SpellingUiState.LoadingError(R.string.loading_fail_text)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saved state with no navBack
     * Back button doesn't show, title: Spelling, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saved_state_test() {
        val state = SpellingUiState.Saved(emptyList())
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertIsDisplayed()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**Saved state with navBack
     * Back button shows, title: Spelling, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saved_state_with_navBack_test() {
        val state = SpellingUiState.Saved(emptyList())
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
            ).assertDoesNotExist()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionAddNew),
                useUnmergedTree = true
            ).assertIsDisplayed()
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**Saving error with no navBack
     * Back button doesn't show, title: Spelling, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saving_error_state_test() {
        val state = SpellingUiState.SavingError(R.string.loading_fail_text, emptyList())
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saving error state with navBack
     * Back button shows, title: Spelling, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saving_error_state_with_navBack_test() {
        val state = SpellingUiState.SavingError(R.string.loading_fail_text, emptyList())
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SpellingTopAppBar(
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    addNewCallback = {},
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
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionRedo),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }
}