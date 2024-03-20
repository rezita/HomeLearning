package com.github.rezita.homelearning.model

import org.junit.Assert
import org.junit.Test

class SentenceStatusTest {
    @Test
    fun sentence_no_answer_unchecked(){
        val sentence = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "",
            tense = "present"
        )
        Assert.assertEquals(sentence.status, WordStatus.UNCHECKED)
    }

    @Test
    fun sentence_correct_answer_correct(){
        val sentence = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "answer",
            tense = "present"
        )
        Assert.assertEquals(sentence.status, WordStatus.CORRECT)
    }

    @Test
    fun sentence_incorrect_answer_incorrect(){
        val sentence = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "answers",
            tense = "present"
        )
        Assert.assertEquals(sentence.status, WordStatus.INCORRECT)
    }


    @Test
    fun sentence_partly_correct_answer_incorrect(){
        val sentence = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "answe",
            tense = "present"
        )
        Assert.assertEquals(sentence.status, WordStatus.INCORRECT)
    }

    @Test
    fun sentence_irrelevant_answer_incorrect(){
        val sentence = FillInSentence(
            sentence = "This is the example",
            suggestion = "answer / wrong",
            solutions = listOf("answer", "correct"),
            answer = "cheese",
            tense = "present"
        )
        Assert.assertEquals(sentence.status, WordStatus.INCORRECT)
    }
}