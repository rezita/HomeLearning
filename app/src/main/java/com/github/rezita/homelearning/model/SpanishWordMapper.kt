package com.github.rezita.homelearning.model

import kotlin.random.Random

fun ApiSpanishWord.asSpanishWord(enToSp: Boolean?): SpanishWord {
    return SpanishWord(
        wordEn = en,
        wordSp = sp,
        isWeekWord = (isWeekWord == 1),
        enToSp = enToSp ?: Random.nextBoolean(),
    )
}

fun SpanishWord.asApiSpanishWord(): ApiSpanishWord {
    return ApiSpanishWord(
        en = wordEn,
        sp = wordSp,
        result = status.value.toString()
    )
}