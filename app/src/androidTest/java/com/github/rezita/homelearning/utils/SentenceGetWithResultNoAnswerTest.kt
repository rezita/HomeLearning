package com.github.rezita.homelearning.utils

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import androidx.core.graphics.toColor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SentenceGetWithResultNoAnswerTest {
    @Test
    fun sentenceWithResult_unchecked_one_solution_no_separator() {
        val sentence = "I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "beenI have never been to Italy. (be)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_unchecked_one_solution_more_separators() {
        val sentence = "I have never $£ to $£ Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have never been to  Italy. (be)"
        val expectedSpanStart = 13
        val expectedSpanEnd = 17
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }


    @Test
    fun sentenceWithResult_unchecked_one_solution_front_separator() {
        val sentence = "$£I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "beenI have never been to Italy. (be)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_unchecked_one_solution_end_separator() {
        val sentence = "I have never been to Italy.$£"
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have never been to Italy.been (be)"
        val expectedSpanStart = sentence.length - 2
        val expectedSpanEnd = expectedSpanStart + 4
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }


    @Test
    fun sentenceWithResult_unchecked_one_solution_middle_separator() {
        val sentence = "I have never $£ to Italy."
        val suggestion = "be"
        val solutions = listOf("been")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
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
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_unchecked_more_solutions_no_separator() {
        val sentence = "I have dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "burned / burntI have dinner yet again! (burn)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 14
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_unchecked_more_solutions_front_separator() {
        val sentence = "$£I have dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "burned / burntI have dinner yet again! (burn)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 14
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_unchecked_more_solutions_end_separator() {
        val sentence = "I have dinner yet again!$£"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have dinner yet again!burned / burnt (burn)"
        val expectedSpanStart = sentence.length - 2
        val expectedSpanEnd = expectedSpanStart + 14
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }


    @Test
    fun sentenceWithResult_unchecked_more_solutions_middle_separator() {
        val sentence = "I have \$£ dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")

        val originalSentence =
            FillInSentence(sentence = sentence, suggestion = suggestion, solutions = solutions)

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have burned / burnt dinner yet again! (burn)"
        val expectedSpanStart = 7
        val expectedSpanEnd = 21
        val nrOfSpans = 1
        assertNotNull(result)
        assertEquals(originalSentence.status, WordStatus.UNCHECKED)
        assertEquals(result.toString(), expectedText)
        assertNotNull(spans)
        assertEquals(spans.size, nrOfSpans)
        assertEquals(span.foregroundColor, Color.RED)
        assertEquals(result.getSpanStart(span), expectedSpanStart)
        assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }
}