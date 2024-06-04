package com.github.rezita.homelearning.model

import com.github.rezita.homelearning.model.ReadingRule
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class SplitReadingRuleTest {

    @Test
    fun readingRuleIsSplitDigraph_no(){
        val rule = ReadingRule(word = "she", pattern = "sh", ruleName = "ul")
        val isSplit = rule.isSplitDigraph()
        assertEquals(isSplit, false)
    }

    @Test
    fun readingRuleIsSplitDigraph_no_wrong_split_char(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i#e", ruleName = "yellow")
        val isSplit = rule.isSplitDigraph()
        assertEquals(isSplit, false)
    }

    @Test
    fun readingRuleIsSplitDigraph_yes_hyphen(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i-e", ruleName = "yellow")
        val isSplit = rule.isSplitDigraph()
        assertEquals(isSplit, true)
    }

    @Test
    fun readingRuleIsSplitDigraph_yes_at(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i@e", ruleName = "yellow")
        val isSplit = rule.isSplitDigraph()
        assertEquals(isSplit, true)
    }

    @Test
    fun readingRuleIsSplitDigraph_yes_questionMark(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i?e", ruleName = "yellow")
        val isSplit = rule.isSplitDigraph()
        assertEquals(isSplit, true)
    }

    @Test
    fun readingRuleIsSplitDigraph_yes_underline(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i_e", ruleName = "yellow")
        val isSplit = rule.isSplitDigraph()
        assertEquals(isSplit, true)
    }
}
