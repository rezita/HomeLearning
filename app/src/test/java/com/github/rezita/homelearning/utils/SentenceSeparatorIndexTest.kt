package com.github.rezita.homelearning.utils

import com.github.rezita.homelearning.model.FillInSentence
import org.junit.Assert.assertEquals
import org.junit.Test

class SentenceSeparatorIndexTest {
    @Test
    fun fillInSentence_with_no_separator() {
        val sentence = "I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = 0
        assertEquals(originalSentence.getSeparatorIndex(), expectedResult)
    }

    @Test
    fun fillInSentence_with_separator_in_the_front() {
        val sentence = "$£I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = 0
        assertEquals(originalSentence.getSeparatorIndex(), expectedResult)
    }

    @Test
    fun fillInSentence_with_separator_middle() {
        val sentence = "I have never $£ to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = 13
        assertEquals(originalSentence.getSeparatorIndex(), expectedResult)
    }
}