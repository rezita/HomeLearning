package com.github.rezita.homelearning.utils

import com.github.rezita.homelearning.model.ReadingRule
import org.junit.Assert
import org.junit.Test

class ReadingRulePatternIndexTest {
    @Test
    fun getStartIndex_valid_hyphen(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i-e", ruleName = "yellow")
        val startIndex = getPatternIndex(rule.word, rule = rule)
        Assert.assertEquals(10, startIndex)
    }

    @Test
    fun getStartIndex_valid_questionMark(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i?e", ruleName = "yellow")
        val startIndex = getPatternIndex(rule.word, rule = rule)
        Assert.assertEquals(10, startIndex)
    }

    @Test
    fun getStartIndex_valid_underline(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i_e", ruleName = "yellow")
        val startIndex = getPatternIndex(rule.word, rule = rule)
        Assert.assertEquals(10, startIndex)
    }

    @Test
    fun getStartIndex_valid_at(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i@e", ruleName = "yellow")
        val startIndex = getPatternIndex(rule.word, rule = rule)
        Assert.assertEquals(10, startIndex)
    }

    @Test
    fun getStartIndex_valid_invalidSign(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "i#e", ruleName = "yellow")
        val startIndex = getPatternIndex(rule.word, rule = rule)
        Assert.assertEquals(-1, startIndex)
    }

    @Test
    fun getStartIndex_valid_not_split(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "ck", ruleName = "yellow")
        val startIndex = getPatternIndex(rule.word, rule = rule)
        Assert.assertEquals(7, startIndex)
    }

    @Test
    fun getStartIndex_valid_noMatch(){
        val rule = ReadingRule(word = "anticlockwise", pattern = "e-i", ruleName = "yellow")
        val startIndex = getPatternIndex(rule.word, rule = rule)
        Assert.assertEquals(-1, startIndex)
    }
}