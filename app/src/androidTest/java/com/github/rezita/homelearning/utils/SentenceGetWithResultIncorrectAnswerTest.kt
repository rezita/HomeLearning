package com.github.rezita.homelearning.utils

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import org.junit.Assert
import org.junit.Test

class SentenceGetWithResultIncorrectAnswerTest {
    @Test
    fun sentenceWithResult_correct_one_solution_no_separator() {
        val sentence = "I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "was"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "was beenI have never been to Italy. (be)"
        val expectedSpan1Start = 0
        val expectedSpan1End = expectedSpan1Start + 3
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 4
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }

    @Test
    fun sentenceWithResult_correct_one_solution_more_separators() {
        val sentence = "I have never $£ to $£ Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "was"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "I have never was been to  Italy. (be)"
        val expectedSpan1Start = 13
        val expectedSpan1End = expectedSpan1Start + 3
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 4
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }


    @Test
    fun sentenceWithResult_correct_one_solution_front_separator() {
        val sentence = "$£I have never been to Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "was"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )
        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "was beenI have never been to Italy. (be)"
        val expectedSpan1Start = 0
        val expectedSpan1End = expectedSpan1Start + 3
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 4
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }

    @Test
    fun sentenceWithResult_correct_one_solution_end_separator() {
        val sentence = "I have never been to Italy.$£"
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "was"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "I have never been to Italy.was been (be)"
        val expectedSpan1Start = sentence.length - 2
        val expectedSpan1End = expectedSpan1Start + 3
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 4
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }


    @Test
    fun sentenceWithResult_correct_one_solution_middle_separator() {
        val sentence = "I have never $£ to Italy."
        val suggestion = "be"
        val solutions = listOf("been")
        val answer = "was"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "I have never was been to Italy. (be)"
        val expectedSpan1Start = 13
        val expectedSpan1End = expectedSpan1Start + 3
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 4
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_no_separator() {
        val sentence = "I have dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")
        val answer = "burn"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "burn burned / burntI have dinner yet again! (burn)"
        val expectedSpan1Start = 0
        val expectedSpan1End = expectedSpan1Start + 4
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 14
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_front_separator() {
        val sentence = "$£I have dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")
        val answer = "burn"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "burn burned / burntI have dinner yet again! (burn)"
        val expectedSpan1Start = 0
        val expectedSpan1End = expectedSpan1Start + 4
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 14
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }

    @Test
    fun sentenceWithResult_correct_more_solutions_end_separator() {
        val sentence = "I have dinner yet again!$£"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")
        val answer = "burn"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "I have dinner yet again!burn burned / burnt (burn)"
        val expectedSpan1Start = sentence.length - 2
        val expectedSpan1End = expectedSpan1Start + 4
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 14
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }


    @Test
    fun sentenceWithResult_correct_more_solutions_middle_separator() {
        val sentence = "I have \$£ dinner yet again!"
        val suggestion = "burn"
        val solutions = listOf("burned", "burnt")
        val answer = "burn"

        val originalSentence =
            FillInSentence(
                sentence = sentence,
                suggestion = suggestion,
                solutions = solutions,
                answer = answer
            )

        val result = originalSentence.getWithResult()
        val spans = result.getSpans(0, result.toString().length, ForegroundColorSpan::class.java)
        /** span for the answer*/
        val span1 = spans[0]
        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "I have burn burned / burnt dinner yet again! (burn)"
        val expectedSpan1Start = 7
        val expectedSpan1End = expectedSpan1Start + 4
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 14
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)
        Assert.assertEquals(span1.foregroundColor, Color.RED)
        Assert.assertEquals(result.getSpanStart(span1), expectedSpan1Start)
        Assert.assertEquals(result.getSpanEnd(span1), expectedSpan1End)
        Assert.assertEquals(span2.foregroundColor, Color.GREEN)
        Assert.assertEquals(result.getSpanStart(span2), expectedSpan2Start)
        Assert.assertEquals(result.getSpanEnd(span2), expectedSpan2End)
    }
}