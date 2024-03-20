package com.github.rezita.homelearning.model

import org.junit.Assert
import org.junit.Test

class SentenceMapperTest {
    @Test
    fun test_sentence_to_APISentence_correctAnswer() {
        val original = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "answer",
            tense = "present"
        )
        val expectedResult = ApiFillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solution = "answer / correct",
            answer = "answer",
            tense = "present",
            result = "1"
        )
        Assert.assertEquals (expectedResult, original.asAPISentence())
    }

    @Test
    fun test_sentence_to_APISentence_incorrectAnswer() {
        val original = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "answers",
            tense = "present"
        )
        val expectedResult = ApiFillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solution = "answer / correct",
            answer = "answers",
            tense = "present",
            result = "-1"
        )
        Assert.assertEquals(original.asAPISentence(), expectedResult)
    }

    @Test
    fun test_sentence_to_APISentence_noAnswer() {
        val original = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "",
            tense = "present"
        )
        val expectedResult = ApiFillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solution = "answer / correct",
            answer = "",
            tense = "present",
            result = "0"
        )
        Assert.assertEquals (expectedResult, original.asAPISentence())
    }

    @Test
    fun test_APISentence_to_sentence_more_solutions() {
        val expectedResult = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "answer",
            tense = "present"
        )
        val original = ApiFillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solution = "answer / correct",
            answer = "answer",
            tense = "present",
            result = "1"
        )
        val result = original.asFillInSentence()
        Assert.assertEquals(result, expectedResult)
        Assert.assertEquals(result.status, WordStatus.CORRECT)
    }

    @Test
    fun test_APISentence_to_sentence_one_solution() {
        val expectedResult = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer"),
            answer = "answer",
            tense = "present",
        )
        val original = ApiFillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solution = "answer",
            answer = "answer",
            tense = "present",
            result = "-1"
        )
        val result = original.asFillInSentence()
        Assert.assertEquals(result, expectedResult)
        Assert.assertEquals(result.status, WordStatus.CORRECT)
    }

    @Test
    fun test_APISentence_to_sentence_incorrect() {
        val expectedResult = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer"),
            answer = "answers2",
            tense = "present",
        )
        val original = ApiFillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solution = "answer",
            answer = "answers2",
            tense = "present",
            result = "-1"
        )
        val result = original.asFillInSentence()
        Assert.assertEquals(result, expectedResult)
        Assert.assertEquals(result.status, WordStatus.INCORRECT)
    }

    @Test
    fun test_APISentence_to_sentence_no_answer() {
        val expectedResult = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer"),
            answer = "",
            tense = "present",
        )
        val original = ApiFillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solution = "answer",
            answer = "",
            tense = "present",
            result = "-1"
        )
        val result = original.asFillInSentence()
        Assert.assertEquals(result, expectedResult)
        Assert.assertEquals(result.status, WordStatus.UNCHECKED)
    }
}