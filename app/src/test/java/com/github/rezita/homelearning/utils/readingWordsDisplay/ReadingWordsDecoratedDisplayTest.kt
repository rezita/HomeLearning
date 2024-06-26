package com.github.rezita.homelearning.utils.readingWordsDisplay

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import com.github.rezita.homelearning.model.ReadingRule
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.utils.getDecorated
import org.junit.Assert
import org.junit.Test


class ReadingWordsDecoratedDisplayTest {

    @Test
    fun readingWord_no_rules_color_display() {
        val word = ReadingWord(
            word = "help",
            category = "CEW1",
            comment = "tricky words",
            rules = emptyList()
        )
        val result = word.getDecorated(Color.Black)
        Assert.assertEquals(result.spanStyles.size, 1)
    }

    @Test
    fun readingWord_one_colour_rule_color_display() {
        val word = ReadingWord(
            word = "day",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "day", pattern = "ay", ruleName = "red")
            )
        )
        val result = word.getDecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[1]
        Assert.assertEquals(spanStyles.size, 2)
        Assert.assertEquals(span1.start, 1)
        Assert.assertEquals(span1.end, 3)
        Assert.assertEquals(span1.item.color, Color(android.graphics.Color.RED))
    }

    @Test
    fun readingWord_one_silent_endee_rule_color_display() {
        val word = ReadingWord(
            word = "have",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "have", pattern = "e", ruleName = "silente")
            )
        )
        val result = word.getDecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[1]
        Assert.assertEquals(spanStyles.size, 2)
        Assert.assertEquals(span1.start, 3)
        Assert.assertEquals(span1.end, 4)
        Assert.assertEquals(span1.item.color, Color(android.graphics.Color.WHITE))
    }

    @Test
    fun readingWord_one_ul_rule_color_display() {
        val word = ReadingWord(
            word = "that",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "that", pattern = "th", ruleName = "ul")
            )
        )
        val result = word.getDecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[1]
        Assert.assertEquals(spanStyles.size, 2)
        Assert.assertEquals(span1.start, 0)
        Assert.assertEquals(span1.end, 2)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.Underline)
    }

    @Test
    fun readingWord_more_rules_color_display() {
        val word = ReadingWord(
            word = "she",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "she", pattern = "sh", ruleName = "ul"),
                ReadingRule(word = "she", pattern = "e", ruleName = "green")
            )
        )
        val result = word.getDecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[1]
        val span2 = spanStyles[2]
        Assert.assertEquals(spanStyles.size, 3)

        Assert.assertEquals(span1.start, 0)
        Assert.assertEquals(span1.end, 2)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.Underline)

        Assert.assertEquals(span2.start, 2)
        Assert.assertEquals(span2.end, 3)
        Assert.assertEquals(span2.item.color, Color(android.graphics.Color.GREEN))
    }

    @Test
    fun readingWord_more_rules_color_display_with_split_digraph() {
        val word = ReadingWord(
            word = "anticlockwise",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "anticlockwise", pattern = "ck", ruleName = "ul"),
                ReadingRule(word = "anticlockwise", pattern = "i-e", ruleName = "yellow")
            )
        )
        val result = word.getDecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[1]
        val span2 = spanStyles[2]
        val span3 = spanStyles[3]
        Assert.assertEquals(spanStyles.size, 4)

        Assert.assertEquals(span1.start, 7)
        Assert.assertEquals(span1.end, 9)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.Underline)

        Assert.assertEquals(span2.start, 10)
        Assert.assertEquals(span2.end, 11)
        Assert.assertEquals(span2.item.color, Color(android.graphics.Color.YELLOW))

        Assert.assertEquals(span3.start, 12)
        Assert.assertEquals(span3.end, 13)
        Assert.assertEquals(span3.item.color, Color(android.graphics.Color.YELLOW))
    }

    @Test
    fun readingWord_one_rule_color_display_split_digraph() {
        val word = ReadingWord(
            word = "make",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "make", pattern = "a-e", ruleName = "red")
            )
        )
        val result = word.getDecorated(Color.Black)
        val spanStyles = result.spanStyles
        val span1 = spanStyles[1]
        val span2 = spanStyles[2]
        Assert.assertEquals(spanStyles.size, 3)

        Assert.assertEquals(span1.start, 1)
        Assert.assertEquals(span1.end, 2)
        Assert.assertEquals(span2.item.color, Color(android.graphics.Color.RED))

        Assert.assertEquals(span2.start, 3)
        Assert.assertEquals(span2.end, 4)
        Assert.assertEquals(span2.item.color, Color(android.graphics.Color.RED))
    }

    @Test
    fun readingWord_one_rule_color_display_split_digraph_wrong_setting() {
        val word = ReadingWord(
            word = "make",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "make", pattern = "a#e", ruleName = "red")
            )
        )
        val result = word.getDecorated(Color.Black)
        val spanStyles = result.spanStyles
        Assert.assertEquals(spanStyles.size, 1)
    }

    @Test
    fun readingWord_one_rule_color_display_split_digraph_no_match() {
        val word = ReadingWord(
            word = "make",
            category = "CEW1",
            comment = "tricky words",
            rules = arrayListOf(
                ReadingRule(word = "make", pattern = "e-a", ruleName = "red")
            )
        )
        val result = word.getDecorated(Color.Black)
        val spanStyles = result.spanStyles
        Assert.assertEquals(spanStyles.size, 1)
    }
}