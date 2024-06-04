package com.github.rezita.homelearning.utils

import com.github.rezita.homelearning.model.ReadingRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class ReadingRuleToRegex {
    @Test
    fun notSplitRuleToRegex() {
        val rule = ReadingRule(word = "she", pattern = "sh", ruleName = "ul")
        val regex = rule.patternToRegex()
        assertEquals("sh", regex.toString())
    }

    @Test
    fun splitRuleToRegex_no_wrong_split_char() {
        val rule = ReadingRule(word = "anticlockwise", pattern = "i#e", ruleName = "yellow")
        val regex = rule.patternToRegex()
        assertEquals("i#e", regex.toString())
    }

    @Test
    fun readingRuleIsSplitDigraph_yes_hyphen() {
        val rule = ReadingRule(word = "anticlockwise", pattern = "i-e", ruleName = "yellow")
        val regex = rule.patternToRegex()
        assertEquals("i[b-df-hj-np-tv-z]e", regex.toString())
    }

    @Test
    fun readingRuleIsSplitDigraph_yes_at() {
        val rule = ReadingRule(word = "anticlockwise", pattern = "i@e", ruleName = "yellow")
        val regex = rule.patternToRegex()
        assertEquals("i[b-df-hj-np-tv-z]e", regex.toString())
    }

    @Test
    fun readingRuleIsSplitDigraph_yes_questionMark() {
        val rule = ReadingRule(word = "anticlockwise", pattern = "i?e", ruleName = "yellow")
        val regex = rule.patternToRegex()
        assertEquals("i[b-df-hj-np-tv-z]e", regex.toString())
    }

    @Test
    fun readingRuleIsSplitDigraph_yes_underline() {
        val rule = ReadingRule(word = "anticlockwise", pattern = "i_e", ruleName = "yellow")
        val regex = rule.patternToRegex()
        assertEquals("i[b-df-hj-np-tv-z]e", regex.toString())
    }
}