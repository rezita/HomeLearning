package com.github.rezita.homelearning.model

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class ReadingRuleConversationTest {
    /*positive path*/
    @Test
    fun readingRule_emptyString() {
        val word = "help"
        val rule = ""
        val readingRule = getRules(word, rule)
        Assert.assertTrue(readingRule.isEmpty())
    }

    @Test
    fun readingRule_one_rule() {
        val word = "you"
        val rule = "blue ou"
        val expectedReadingRule =
            arrayListOf(ReadingRule(word = "you", subWord = "ou", ruleName = "blue"))
        val readingRule = getRules(word, rule)
        assertEquals(expectedReadingRule, readingRule)
    }

    @Test
    fun readingRule_two_rules_with_space() {
        val word = "she"
        val rule = "ul sh; green e"
        val expectedReadingRule = arrayListOf(
            ReadingRule(word = "she", subWord = "sh", ruleName = "ul"),
            ReadingRule(word = "she", subWord = "e", ruleName = "green")
        )
        val readingRule = getRules(word, rule)
        assertEquals(expectedReadingRule, readingRule)
    }

    @Test
    fun readingRule_rule_in_subwords() {
        val word = "look book"
        val rule = "look gold oo"
        val expectedReadingRule = arrayListOf(
            ReadingRule(word = "look", subWord = "oo", ruleName = "gold")
        )
        val readingRule = getRules(word, rule)
        assertEquals(expectedReadingRule, readingRule)
    }

    @Test
    fun readingRule_two_rules_wo_space() {
        val word = "she"
        val rule = "ul sh;green e"
        val expectedReadingRule = arrayListOf(
            ReadingRule(word = "she", subWord = "sh", ruleName = "ul"),
            ReadingRule(word = "she", subWord = "e", ruleName = "green")
        )
        val readingRule = getRules(word, rule)
        assertEquals(expectedReadingRule, readingRule)
    }

    /*error path*/
    @Test
    fun readingRule_empty_word() {
        val word = ""
        val rule = "ul sh;green e"
        val expectedReadingRule = arrayListOf<ReadingRule>(
            ReadingRule(word = "", subWord = "sh", ruleName = "ul"),
            ReadingRule(word = "", subWord = "e", ruleName = "green")
        )
        val readingRule = getRules(word, rule)
        assertEquals(expectedReadingRule, readingRule)
    }

    @Test
    fun readingRule_wrong_separator() {
        val word = "help"
        val rule = ","
        val readingRule = getRules(word, rule)
        Assert.assertTrue(readingRule.isEmpty())
    }

    @Test
    fun readingRule_with_one_part() {
        val word = "help"
        val rule = "gold"
        val readingRule = getRules(word, rule)
        Assert.assertTrue(readingRule.isEmpty())
    }

    @Test
    fun readingRule_with_four_parts() {
        val word = "help"
        val rule = "gold o l d"
        val readingRule = getRules(word, rule)
        Assert.assertTrue(readingRule.isEmpty())
    }


    /**boundary cases**/
    @Test
    fun readingRule_emptyRules() {
        val word = "help"
        val rule = ";"
        val readingRule = getRules(word, rule)
        Assert.assertTrue(readingRule.isEmpty())
    }

    @Test
    fun readingRule_two_rules_second_empty() {
        val word = "she"
        val rule = "ul sh;"
        val expectedReadingRule = arrayListOf(
            ReadingRule(word = "she", subWord = "sh", ruleName = "ul"),
        )
        val readingRule = getRules(word, rule)
        assertEquals(expectedReadingRule, readingRule)
    }
}