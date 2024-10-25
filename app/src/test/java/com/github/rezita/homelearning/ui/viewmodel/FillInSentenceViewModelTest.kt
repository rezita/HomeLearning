package com.github.rezita.homelearning.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.*
import app.cash.turbine.test
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.fake.FakeNetworkWorkRepository
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.navigation.SentenceDestination
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.rules.TestDispatcherRule
import com.github.rezita.homelearning.ui.screens.sentence.SentenceUiState
import com.github.rezita.homelearning.ui.viewmodels.FillInSentenceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FillInSentenceViewModelTest {

    private val sentence1 = FillInSentence(
        sentence = "I don’t know what’s wrong – I just feel $£.",
        suggestion = "blue / blew",
        solutions = listOf("blue"),
        answer = "",
        tense = "Y2"
    )
    private val sentence2 = FillInSentence(
        sentence = "The rational and the irrational $£ each other.",
        suggestion = "complement / compliment",
        solutions = listOf("complement"),
        answer = "",
        tense = "Y5-6"
    )

    private val sentence3 = FillInSentence(
        sentence = "It may have an $£ on our plans.",
        suggestion = "affect / effect",
        solutions = listOf("effect"),
        answer = "",
        tense = "Y3-4"
    )

    private val sentences =
        listOf(sentence1, sentence2, sentence3)

    @get:Rule
    val testDispatcher = TestDispatcherRule()
    private lateinit var fillInSentenceViewModel: FillInSentenceViewModel
    private lateinit var fakeRepository: WordRepository


    @Before
    fun setUp() {
        fakeRepository = FakeNetworkWorkRepository()

        runTest {
            fillInSentenceViewModel = FillInSentenceViewModel(
                savedStateHandle = SavedStateHandle.Companion.(
                    route = SentenceDestination(SheetAction.READ_HOMOPHONES)
                ),
                wordRepository = fakeRepository
            )
        }
    }

    /**Answers all questions and returns the expected updated list*/
    fun answerAllQuestions(): List<FillInSentence> {
        val answer = "answer"
        var newSentences = sentences.toMutableList()
        //setting answer for the sentences
        runTest {
            for (i in sentences.indices) {
                fillInSentenceViewModel.updateAnswer(i, answer)
                newSentences[i] = sentences[i].copy(answer = answer)
            }
        }
        return newSentences
    }

    /**Test that the load() function works using Turbine*/
    @Test
    fun load_valid_data_test() = runTest {
        fillInSentenceViewModel.uiState.test {
            fillInSentenceViewModel.load()
            //because the sentences were already loaded
            assertEquals(SentenceUiState.Loaded(sentences), awaitItem())
            //during loading
            assertEquals(SentenceUiState.Loading, awaitItem())
            //after loading
            assertEquals(SentenceUiState.Loaded(sentences), awaitItem())
        }
    }

    @Test
    fun after_init_valid_data_test() {
        val stateAfterInit = fillInSentenceViewModel.uiState.value
        assertEquals(stateAfterInit, SentenceUiState.Loaded(sentences = sentences))
        Assert.assertFalse(stateAfterInit.isSavable())
    }

    @Test
    fun after_load_valid_data_test() =
        runTest {
            fillInSentenceViewModel.load()
            delay(1000)
            val stateAfterLoad = fillInSentenceViewModel.uiState.value
            assertEquals(SentenceUiState.Loaded(sentences = sentences), stateAfterLoad)
            Assert.assertFalse(stateAfterLoad.isSavable())
        }


    @Test
    fun update_answer_valid_data_test() {
        val index = 0
        val answer = "answer"
        runTest {
            fillInSentenceViewModel.updateAnswer(index, answer)
        }
        val newSentences =
            sentences.toMutableList().apply { this[index] = this[index].copy(answer = answer) }
        val stateAfterUpdate = fillInSentenceViewModel.uiState.value
        assertEquals(SentenceUiState.Loaded(sentences = newSentences), stateAfterUpdate)
    }

    @Test
    fun update_answer_outOfBound_test() {
        val index = 3
        val answer = "answer"
        Assert.assertThrows(IllegalArgumentException::class.java) {
            fillInSentenceViewModel.updateAnswer(index, answer)

        }

        val uiState = fillInSentenceViewModel.uiState.value
        assertEquals(uiState, SentenceUiState.Loaded(sentences = sentences))
    }

    @Test
    fun save_sentences_answer_all_test() {
        val newSentences = answerAllQuestions()
        val state0 = fillInSentenceViewModel.uiState.value
        assertEquals(state0, SentenceUiState.Loaded(sentences = newSentences))

        val allAnswered = fillInSentenceViewModel.uiState.value.isSavable()
        Assert.assertTrue(allAnswered)
    }

    @Test
    fun save_sentences_not_all_answered_test() {
        runTest {
            fillInSentenceViewModel.saveSentences()
        }
        val stateAfterSave = fillInSentenceViewModel.uiState.value
        assertEquals(stateAfterSave, SentenceUiState.Loaded(sentences = sentences))
    }


    @Test
    fun save_sentences_save_all_answered_test() {
        val newSentences = answerAllQuestions()
        runTest {
            fillInSentenceViewModel.saveSentences()
        }

        val stateAfterSave = fillInSentenceViewModel.uiState.value
        assertEquals(stateAfterSave, SentenceUiState.Saved(sentences = newSentences))
    }

}