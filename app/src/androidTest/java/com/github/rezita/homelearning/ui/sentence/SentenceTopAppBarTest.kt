package com.github.rezita.homelearning.ui.sentence

import android.content.Context
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.ui.screens.sentence.SentenceTopAppBar
import com.github.rezita.homelearning.ui.screens.sentence.SentenceUiState
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.util.buttonWithImageAndDescription
import com.github.rezita.homelearning.ui.util.withRole
import org.junit.Rule
import org.junit.Test

class SentenceTopAppBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val titleId = R.string.homophones_title
    private val actionSave = context.getText(R.string.sentences_check_and_save).toString()

    /**Loading state with no navBack
     * Back button doesn't show, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_state_test() {
        val state = SentenceUiState.Loading
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
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
    }

    /**Loading state with navBack
     * Back button shows, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_state_with_navBack_test() {
        val state = SentenceUiState.Loading
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with navBack, empty sentences
     * Back button shows, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_emptyList_state_with_navBack_test() {
        val state = SentenceUiState.Loaded(emptyList())
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loaded state with no navBack and savable (all answered)
     * Back button doesn't show, title: Homophones, action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_savable_state_test() {
        val sentences = listOf(
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            ),
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )
        )
        val state = SentenceUiState.Loaded(sentences)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
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
    }

    /**Loaded state with navBack and savable (all answered)
     * Back button shows, title: Homophones, action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_savable_navBack_state_test() {
        val sentences = listOf(
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            ),
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )
        )
        val state = SentenceUiState.Loaded(sentences)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(2)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    /**Loaded state with no navBack and not savable
     * Back button doesn't show, title: Homophones, action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_not_savable_state_test() {
        val sentences = listOf(
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = ""
            ),
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )
        )
        val state = SentenceUiState.Loaded(sentences)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
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
    }

    /**Loaded state with navBack and not savable
     * Back button shows, title: Homophones, action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loaded_not_savable_navBack_state_test() {
        val sentences = listOf(
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = ""
            ),
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )
        )
        val state = SentenceUiState.Loaded(sentences)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
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
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Loading error state with no navBack
     * Back button doesn't show, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_error_state_test() {
        val state = SentenceUiState.LoadingError(R.string.upload_error_word_message)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
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
    }

    /**Loading error state with navBack
     * Back button shows, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_error_state_with_navBack_test() {
        val state = SentenceUiState.LoadingError(R.string.upload_error_word_message)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saved state with no navBack
     * Back button doesn't show, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saved_state_test() {
        val state = SentenceUiState.Saved(emptyList())
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
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
    }

    /**Saved state with navBack
     * Back button shows, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_loading_saved_state_with_navBack_test() {
        val state = SentenceUiState.Saved(emptyList())
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saving error state with no navBack empty sentences
     * Back button doesn't show, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saving_error_empty_state_test() {
        val state = SentenceUiState.SavingError(R.string.upload_no_words_message, emptyList())
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
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
    }

    /**Saving error state with navBack empty sentences
     * Back button doesn't show, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saving_error_state_empty_navBack_test() {
        val state = SentenceUiState.SavingError(R.string.upload_no_words_message, emptyList())
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }

    /**Saving error state with no navBack
     * Back button shows, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saving_error_state_test() {
        val sentences = listOf(
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )
        )
        val state = SentenceUiState.SavingError(R.string.upload_no_words_message, sentences)
        val canNavigateBack = false

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
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
    }

    /**Saving error state with navBack
     * Back button shows, title: Homophones, no action button shows*/
    @SmallTest
    @Test
    fun sentenceTopAppBar_saving_error_state_with_navBack_test() {
        val sentences = listOf(
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )
        )
        val state = SentenceUiState.SavingError(R.string.upload_no_words_message, sentences)
        val canNavigateBack = true

        composeTestRule.setContent {
            HomeLearningTheme {
                SentenceTopAppBar(
                    titleId = titleId,
                    state = state,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { },
                    callback = {},
                )
            }
        }

        //Title
        composeTestRule.onNodeWithText(text = context.getText(titleId).toString())
        //There is no button
        composeTestRule.onAllNodes(withRole(Role.Button)).assertCountEquals(1)
        //no navBack
        composeTestRule
            .onNode(
                buttonWithImageAndDescription("Back"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        //no ActionButtons displayed
        composeTestRule
            .onNode(
                buttonWithImageAndDescription(actionSave),
                useUnmergedTree = true
            ).assertDoesNotExist()
    }
}