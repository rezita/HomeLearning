package com.github.rezita.homelearning.utils

import com.github.rezita.homelearning.model.FillInSentence
import org.junit.Assert
import org.junit.Test

class SentenceSplitBySeparatorTest {
    @Test
    fun splitBySeparator_middle(){
        val sentence = "I have never $£ to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = Pair("I have never ", " to Italy. (be)")
        Assert.assertEquals(originalSentence.splitBySeparatorWithSuggestion(), expectedResult)
    }

    @Test
    fun splitBySeparator_front(){
        val sentence = "\$£I have never to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = Pair("", " I have never to Italy. (be)")
        Assert.assertEquals(originalSentence.splitBySeparatorWithSuggestion(), expectedResult)
    }

    @Test
    fun splitBySeparator_end(){
        val sentence = "I have never to Italy.\$£"
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = Pair("I have never to Italy. ", " (be)")
        Assert.assertEquals(originalSentence.splitBySeparatorWithSuggestion(), expectedResult)
    }

    @Test
    fun splitBySeparator_more_than_one_separator(){
        val sentence = "I have $£ never $£ to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = Pair("I have ", " never \$£ to Italy. (be)")
        Assert.assertEquals(originalSentence.splitBySeparatorWithSuggestion(), expectedResult)
    }

    @Test
    fun splitBySeparator_no_separator(){
        val sentence = "I have never to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)
        val expectedResult = Pair("", " I have never to Italy. (be)")
        Assert.assertEquals(originalSentence.splitBySeparatorWithSuggestion(), expectedResult)
    }

}