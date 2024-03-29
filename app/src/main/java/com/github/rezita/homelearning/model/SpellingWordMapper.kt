package com.github.rezita.homelearning.model

fun ApiSpellingWord.asSpellingWord(): SpellingWord {
    return SpellingWord(
        word = word,
        category = category,
        comment = comment,
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