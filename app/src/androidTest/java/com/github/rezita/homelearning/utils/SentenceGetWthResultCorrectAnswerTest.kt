package com.github.rezita.homelearning.utils

import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SentenceGetWthResultCorrectAnswerTest {

    @Test
    fun sentenceWithResult_correct_one_solution_no_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "been"
            )

        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "been I have never been to Italy. (be)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }

    @Test
    fun sentenceWithResult_correct_one_solution_more_separators() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never $£ to $£ Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "been"
            )

        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have never been to $£ Italy. (be)"
        val expectedSpanStart = 13
        val expectedSpanEnd = 17
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }


    @Test
    fun sentenceWithResult_correct_one_solution_front_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "$£I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "been"
            )
        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "been I have never been to Italy. (be)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }

    @Test
    fun sentenceWithResult_correct_one_solution_end_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never been to Italy.$£",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "been"
            )

        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have never been to Italy. been (be)"
        val expectedSpanStart = originalSentence.sentence.length - 1
        val expectedSpanEnd = expectedSpanStart + 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }


    @Test
    fun sentenceWithResult_correct_one_solution_middle_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never $£ to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "been"
            )

        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have never been to Italy. (be)"
        val expectedSpanStart = 13
        val expectedSpanEnd = 17
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_no_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt"),
                answer = "burned"
            )

        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "burned I have dinner yet again! (burn)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 6
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_front_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "$£I have dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt"),
                answer = "burned"
            )

        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "burned I have dinner yet again! (burn)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 6
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_end_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have dinner yet again!$£",
                suggestion = "burn",
                solutions = listOf("burned", "burnt"),
                answer = "burned"
            )

        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have dinner yet again! burned (burn)"
        val expectedSpanStart = originalSentence.sentence.length - 1
        val expectedSpanEnd = expectedSpanStart + 6
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }


    @Test
    fun sentenceWithResult_correct_more_solutions_middle_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have \$£ dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt"),
                answer = "burned"
            )

        val result = originalSentence.getWithResult()
        val spans = result.spanStyles
        val span = spans[0]
        val expectedText = "I have burned dinner yet again! (burn)"
        val expectedSpanStart = 7
        val expectedSpanEnd = 13
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.CORRECT)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.start, expectedSpanStart)
        assertEquals(span.end, expectedSpanEnd)
        assertEquals(span.item.color, androidx.compose.ui.graphics.Color.Green)
    }


}