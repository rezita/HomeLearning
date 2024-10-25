package com.github.rezita.homelearning.model

import android.util.Log

fun ApiSpellingWord.asSpellingWord(): SpellingWord {
    return SpellingWord(
        word = word,
        category = category,
        comment = comment,
        repeated = (repeat == 1)
    )
}

fun SpellingWord.asAPISellingWord(): ApiSpellingWord {
    return ApiSpellingWord(
        word = word,
        category = category,
        comment = comment,
        result = status.value.toString()
    )
}