package com.github.rezita.homelearning.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import org.junit.Assert
import org.junit.Test

class SentenceGetWithResultIncorrectAnswerTest {
    @Test
    fun sentenceWithResult_incorrect_one_solution_no_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

        /** span for the answer*/
        val span1 = spans[0]

        /** span for the correct soultion(s)*/
        val span2 = spans[1]

        val expectedText = "was been I have never been to Italy. (be)"
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

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)
    }

    @Test
    fun sentenceWithResult_incorrect_one_solution_more_separators() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never $£ to $£ Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

        /** span for the answer*/
        val span1 = spans[0]

        /** span for the correct soultion(s)*/
        val span2 = spans[1]

        val expectedText = "I have never was been to $£ Italy. (be)"

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

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)
    }


    @Test
    fun sentenceWithResult_incorrect_one_solution_front_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "$£I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )
        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

        /** span for the answer*/
        val span1 = spans[0]

        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "was been I have never been to Italy. (be)"
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

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)
    }

    @Test
    fun sentenceWithResult_incorrect_one_solution_end_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never been to Italy.$£",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

        /** span for the answer*/
        val span1 = spans[0]

        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "I have never been to Italy. was been (be)"
        val expectedSpan1Start = originalSentence.sentence.length - 1
        val expectedSpan1End = expectedSpan1Start + 3
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 4
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)
    }


    @Test
    fun sentenceWithResult_incorrect_one_solution_middle_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have never $£ to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = "was"
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

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

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)
    }

    @Test
    fun sentenceWithResult_incorrect_more_solutions_no_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt"),
                answer = "burn"
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

        /** span for the answer*/
        val span1 = spans[0]

        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "burn burned/burnt I have dinner yet again! (burn)"
        val expectedSpan1Start = 0
        val expectedSpan1End = expectedSpan1Start + 4
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 12
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)
    }

    @Test
    fun sentenceWithResult_incorrect_more_solutions_front_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "$£I have dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt"),
                answer = "burn"
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

        /** span for the answer*/
        val span1 = spans[0]

        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "burn burned/burnt I have dinner yet again! (burn)"
        val expectedSpan1Start = 0
        val expectedSpan1End = expectedSpan1Start + 4
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 12
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)


    }

    @Test
    fun sentenceWithResult_incorrect_more_solutions_end_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have dinner yet again!$£",
                suggestion = "burn",
                solutions = listOf("burned", "burnt"),
                answer = "burn"
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

        /** span for the answer*/
        val span1 = spans[0]

        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "I have dinner yet again! burn burned/burnt (burn)"
        val expectedSpan1Start = originalSentence.sentence.length - 1
        val expectedSpan1End = expectedSpan1Start + 4
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 12
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)
    }


    @Test
    fun sentenceWithResult_incorrect_more_solutions_middle_separator() {
        val originalSentence =
            FillInSentence(
                sentence = "I have \$£ dinner yet again!",
                suggestion = "burn",
                solutions = listOf("burned", "burnt"),
                answer = "burn"
            )

        val result = originalSentence.getWithResult(Color.Green, Color.Red)
        val spans = result.spanStyles

        /** span for the answer*/
        val span1 = spans[0]

        /** span for the correct soultion(s)*/
        val span2 = spans[1]
        val expectedText = "I have burn burned/burnt dinner yet again! (burn)"
        val expectedSpan1Start = 7
        val expectedSpan1End = expectedSpan1Start + 4
        val expectedSpan2Start = expectedSpan1End + 1
        val expectedSpan2End = expectedSpan2Start + 12
        val nrOfSpans = 2
        Assert.assertNotNull(result)
        Assert.assertEquals(originalSentence.status, WordStatus.INCORRECT)
        Assert.assertEquals(result.toString(), expectedText)
        Assert.assertNotNull(spans)
        Assert.assertEquals(spans.size, nrOfSpans)

        Assert.assertEquals(span1.start, expectedSpan1Start)
        Assert.assertEquals(span1.end, expectedSpan1End)
        Assert.assertEquals(span1.item.color, Color.Red)
        Assert.assertEquals(span1.item.textDecoration, TextDecoration.LineThrough)

        Assert.assertEquals(span2.start, expectedSpan2Start)
        Assert.assertEquals(span2.end, expectedSpan2End)
        Assert.assertEquals(span2.item.color, Color.Green)
    }
}