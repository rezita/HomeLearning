package com.github.rezita.homelearning.utils.sentenceDisplay

import androidx.compose.ui.graphics.Color
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.utils.getWithResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SentenceGetWithResultNoAnswerTest {

    @Test
    fun sentenceWithResult_unchecked_one_solution_no_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been")
            )
        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]

        val expectedText = "been I have never been to Italy. (be)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)

        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }

    @Test
    fun sentenceWithResult_unchecked_one_solution_more_separators() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never $£ to $£ Italy.",
                suggestion = "be",
                solutions = listOf("been")
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have never been to $£ Italy. (be)"
        val expectedSpanStart = 13
        val expectedSpanEnd = 17
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }

    @Test
    fun sentenceWithResult_unchecked_one_solution_front_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "$£I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been")
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "been I have never been to Italy. (be)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }

    @Test
    fun sentenceWithResult_unchecked_one_solution_end_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never been to Italy.$£",
                suggestion = "be",
                solutions = listOf("been")
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have never been to Italy. been (be)"
        val expectedSpanStart = originalSentence.sentence.length - 1
        val expectedSpanEnd = expectedSpanStart + 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }

    @Test
    fun sentenceWithResult_unchecked_one_solution_middle_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never $£ to Italy.",
                suggestion = "be",
                solutions = listOf("been")
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have never been to Italy. (be)"
        val expectedSpanStart = 13
        val expectedSpanEnd = 17
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }

    @Test
    fun sentenceWithResult_unchecked_more_solutions_no_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt")
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "burned/burnt I have dinner yet again! (burn)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 12
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }

    @Test
    fun sentenceWithResult_unchecked_more_solutions_front_separator() {

        val originalSentence =
            FillInSentence(
                sentence = "$£I have dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt")
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "burned/burnt I have dinner yet again! (burn)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 12
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }

    @Test
    fun sentenceWithResult_unchecked_more_solutions_end_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have dinner yet again!$£",
                suggestion = "burn",
                solutions = listOf("burned", "burnt")
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have dinner yet again! burned/burnt (burn)"
        val expectedSpanStart = originalSentence.sentence.length - 1
        val expectedSpanEnd = expectedSpanStart + 12
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }


    @Test
    fun sentenceWithResult_unchecked_more_solutions_middle_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have \$£ dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt")
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have burned/burnt dinner yet again! (burn)"
        val expectedSpanStart = 7
        val expectedSpanEnd = 19
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, Color.Red)
    }
}