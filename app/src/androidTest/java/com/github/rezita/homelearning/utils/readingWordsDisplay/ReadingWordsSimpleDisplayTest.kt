package com.github.rezita.homelearning.utils.readingWordsDisplay

import com.github.rezita.homelearning.model.ReadingRule
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.utils.getForBlackDisplay
import org.junit.Assert
import org.junit.Test

class ReadingWordsSimpleDisplayTest {

    @Test
    fun readingWord_no_rules_black_display() {
        val word = ReadingWord(
            word = "help",
            category = "CEW1",
            comment = "tricky words",
            rules = emptyList()
        )
        val result = word.getForBlackDisplay()
        Assert.assertEquals(result.spanStyles.size, 0)
    }

    @Test
    fun readingWord_one_colour_rule_black_display() {
        val word = ReadingWord(
            word = "now",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "now", subWord = "ow", ruleName = "brown")
            )
        )
        val result = word.getForBlackDisplay()
        Assert.assertEquals(result.spanStyles.size, 0)
    }

    @Test
    fun readingWord_one_silent_endee_rule_black_display() {
        val word = ReadingWord(
            word = "have",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "have", subWord = "e", ruleName = "silente")
            )
        )
        val result = word.getForBlackDisplay()
        Assert.assertEquals(result.spanStyles.size, 0)
    }

    @Test
    fun readingWord_one_ul_rule_black_display() {
        val word = ReadingWord(
            word = "that",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "that", subWord = "th", ruleName = "ul")
            )
        )
        val result = word.getForBlackDisplay()
        Assert.assertEquals(result.spanStyles.size, 0)
    }

    @Test
    fun readingWord_more_rules_black_display() {
        val word = ReadingWord(
            word = "she",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "she", subWord = "sh", ruleName = "ul"),
                ReadingRule(word = "she", subWord = "e", ruleName = "green")
            )
        )
        val result = word.getForBlackDisplay()
        Assert.assertEquals(result.spanStyles.size, 0)
    }
}