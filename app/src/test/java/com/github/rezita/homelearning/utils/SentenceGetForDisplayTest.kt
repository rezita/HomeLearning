package com.github.rezita.homelearning.utils

import com.github.rezita.homelearning.model.FillInSentence
import org.junit.Assert
import org.junit.Test

class SentenceGetForDisplayTest {
    @Test
    fun fillInSentence_with_no_separator() {
        val sentence = "I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = "I have never been to Italy. (be)"
        Assert.assertEquals(originalSentence.getForDisplay(), expectedResult)
    }

    @Test
    fun fillInSentence_with_separator() {
        val sentence = "I have never $£ to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = "I have never   to Italy. (be)"
        Assert.assertEquals(originalSentence.getForDisplay(), expectedResult)
    }

    @Test
    fun fillInSentence_with_front_separator() {
        val sentence = "$£ I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = "  I have never been to Italy. (be)"
        Assert.assertEquals(originalSentence.getForDisplay(), expectedResult)
    }

    @Test
    fun fillInSentence_with_front_separator_no_space() {
        val sentence = "$£I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = " I have never been to Italy. (be)"
        Assert.assertEquals(originalSentence.getForDisplay(), expectedResult)
    }

    @Test
    fun fillInSentence_with_end_separator() {
        val sentence = "I have never been to Italy. $£"
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = "I have never been to Italy.   (be)"
        Assert.assertEquals(originalSentence.getForDisplay(), expectedResult)
    }

    @Test
    fun fillInSentence_with_end_separator_no_space() {
        val sentence = "I have never been to Italy.$£"
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = "I have never been to Italy.  (be)"
        Assert.assertEquals(originalSentence.getForDisplay(), expectedResult)
    }
}