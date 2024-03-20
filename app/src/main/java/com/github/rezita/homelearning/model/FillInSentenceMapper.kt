package com.github.rezita.homelearning.model

import com.github.rezita.homelearning.utils.joinWithSeparator
import com.github.rezita.homelearning.utils.toListBySeparator

fun ApiFillInSentence.asFillInSentence() = FillInSentence(
    sentence = sentence,
    suggestion = suggestion,
    solutions = solution.toListBySeparator(SOLUTION_SEPARATOR),
    answer = answer,
    tense = tense
)

fun FillInSentence.asAPISentence() = ApiFillInSentence(
    sentence = sentence,
    suggestion = suggestion,
    solution = solutions.joinWithSeparator(SOLUTION_SEPARATOR),
    answer = answer,
    tense = tense,
    result = status.value.toString()
)