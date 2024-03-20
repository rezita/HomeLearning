package com.github.rezita.homelearning.utils

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import org.junit.Assert
import org.junit.Test

class SentenceGetWthResultCorrectAnswerTest {
    @Test
    fun sentenceWithResult_correct_one_solution_no_separator() {
        val sentence = "I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "been"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "beenI have never been to Italy. (be)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 4
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_correct_one_solution_more_separators() {
        val sentence = "I have never $£ to $£ Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "been"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have never been to  Italy. (be)"
        val expectedSpanStart = 13
        val expectedSpanEnd = 17
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }


    @Test
    fun sentenceWithResult_correct_one_solution_front_separator() {
        val sentence = "$£I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "been"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )
        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "beenI have never been to Italy. (be)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 4
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_correct_one_solution_end_separator() {
        val sentence = "I have never been to Italy.$£"
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "been"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have never been to Italy.been (be)"
        val expectedSpanStart = sentence.length - 2
        val expectedSpanEnd = expectedSpanStart + 4
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }


    @Test
    fun sentenceWithResult_correct_one_solution_middle_separator() {
        val sentence = "I have never $£ to Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "been"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have never been to Italy. (be)"
        val expectedSpanStart = 13
        val expectedSpanEnd = 17
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_no_separator() {
        val sentence = "I have dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")
        val answer = "burned"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "burnedI have dinner yet again! (burn)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 6
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_front_separator() {
        val sentence = "$£I have dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")
        val answer = "burned"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "burnedI have dinner yet again! (burn)"
        val expectedSpanStart = 0
        val expectedSpanEnd = 6
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_end_separator() {
        val sentence = "I have dinner yet again!$£"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")
        val answer = "burned"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have dinner yet again!burned (burn)"
        val expectedSpanStart = sentence.length - 2
        val expectedSpanEnd = expectedSpanStart + 6
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }


    @Test
    fun sentenceWithResult_correct_more_solutions_middle_separator() {
        val sentence = "I have \$£ dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")
        val answer = "burned"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        val span = spans[0]
        val expectedText = "I have burned dinner yet again! (burn)"
        val expectedSpanStart = 7
        val expectedSpanEnd = 13
        val nrOfSpans = 1
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.CORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span), expectedSpanStart)
        Assert.assertEquals(result.getSpanEnd(span), expectedSpanEnd)
    }
}