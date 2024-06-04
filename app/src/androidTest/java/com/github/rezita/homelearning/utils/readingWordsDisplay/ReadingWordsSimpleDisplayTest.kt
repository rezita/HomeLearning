package com.github.rezita.homelearning.utils.readingWordsDisplay

import androidx.compose.ui.graphics.Color
import com.github.rezita.homelearning.model.ReadingRule
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.utils.getUndecorated
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
        val result = word.getUndecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[0]
        Assert.assertEquals(result.spanStyles.size, 1)
        Assert.assertEquals(span1.start, 0)
        Assert.assertEquals(span1.end, 4)
        Assert.assertEquals(span1.item.color, Color(android.graphics.Color.BLACK))
    }

    @Test
    fun readingWord_one_colour_rule_black_display() {
        val word = ReadingWord(
            word = "now",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "now", pattern = "ow", ruleName = "brown")
            )
        )
        val result = word.getUndecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[0]
        Assert.assertEquals(result.spanStyles.size, 1)
        Assert.assertEquals(span1.start, 0)
        Assert.assertEquals(span1.end, 3)
        Assert.assertEquals(span1.item.color, Color(android.graphics.Color.BLACK))

    }

    @Test
    fun readingWord_one_silent_endee_rule_black_display() {
        val word = ReadingWord(
            word = "have",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "have", pattern = "e", ruleName = "silente")
            )
        )
        val result = word.getUndecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[0]
        Assert.assertEquals(result.spanStyles.size, 1)
        Assert.assertEquals(span1.start, 0)
        Assert.assertEquals(span1.end, 4)
        Assert.assertEquals(span1.item.color, Color(android.graphics.Color.BLACK))
    }

    @Test
    fun readingWord_one_ul_rule_black_display() {
        val word = ReadingWord(
            word = "that",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "that", pattern = "th", ruleName = "ul")
            )
        )
        val result = word.getUndecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[0]
        Assert.assertEquals(result.spanStyles.size, 1)
        Assert.assertEquals(span1.start, 0)
        Assert.assertEquals(span1.end, 4)
        Assert.assertEquals(span1.item.color, Color(android.graphics.Color.BLACK))

    }

    @Test
    fun readingWord_more_rules_black_display() {
        val word = ReadingWord(
            word = "she",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "she", pattern = "sh", ruleName = "ul"),
                ReadingRule(word = "she", pattern = "e", ruleName = "green")
            )
        )
        val result = word.getUndecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[0]
        Assert.assertEquals(result.spanStyles.size, 1)
        Assert.assertEquals(span1.start, 0)
        Assert.assertEquals(span1.end, 3)
        Assert.assertEquals(span1.item.color, Color(android.graphics.Color.BLACK))

    }
}