package com.github.rezita.homelearning.model

import org.junit.Assert
import org.junit.Test

class ReadingMapperTest {
    @Test
    fun api_reading_word_to_reading_word_empty_word() {
        val apiWord = ApiReadingWord(word = "", category = "", comment = "", rule = "")
        val expectedWord = ReadingWord(word = "", category = "", comment = "", rules = emptyList())
        Assert.assertEquals(expectedWord, apiWord.asReadingWord())
    }

    @Test
    fun api_reading_word_to_reading_word_proper_word() {
        val apiWord = ApiReadingWord(
            word = "she",
            comment = "tricky words",
            rule = "ul sh; green e",
            category = "phase2"
        )
        val expectedWord = ReadingWord(
            word = "she",
            category = "phase2",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "she", pattern = "sh", ruleName = "ul"),
                ReadingRule(word = "she", pattern = "e", ruleName = "green")
            )
        )
        Assert.assertEquals(expectedWord, apiWord.asReadingWord())
    }
}