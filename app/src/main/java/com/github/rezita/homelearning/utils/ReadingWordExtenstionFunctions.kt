package com.github.rezita.homelearning.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import com.github.rezita.homelearning.model.ReadingRule
import com.github.rezita.homelearning.model.ReadingWord

fun ReadingWord.getForDisplay(): SpannableString {
    val spannable = SpannableString(word)
    for (rule in rules) {
        if (rule.isSilentEndE()) {
            if(spannable[spannable.length -1].toString().equals("e", ignoreCase = false)){
                setColorDecoration(spannable, rule, spannable.length - 1, 1)
            }
        } else {
            val startIndex = word.indexOf(rule.subWord, 0, false)
            if (startIndex != -1) {
                if (rule.isUnderline()) {
                    setUnderlineDecoration(spannable, startIndex, rule.subWord.length)
                } else {
                    setColorDecoration(spannable, rule, startIndex, rule.subWord.length)
                }
            }
        }
    }
    return spannable
}

private fun setColorDecoration(
    text: SpannableString,
    rule: ReadingRule,
    startIndex: Int,
    length: Int
) {
    text.setSpan(
        ForegroundColorSpan(rule.getRuleColor()),
        startIndex,
        startIndex + length,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
}

private fun setUnderlineDecoration(
    text: SpannableString,
    startIndex: Int,
    length: Int
) {
    text.setSpan(
        UnderlineSpan(),
        startIndex,
        startIndex + length,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
}