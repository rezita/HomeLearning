package com.github.rezita.homelearning.model

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan

fun ReadingWord.getDecorated(): SpannableString {
    val spannable = SpannableString(word)
    val rules = getRulesArray(this)
    for (rule in rules) {
        if (rule.isSilentEndE()) {
            setColorDecoration(spannable, rule, spannable.length - 1, 1)
        } else {
            val startIndex = word.indexOf(rule.subWord, 0, false)
            if (startIndex != -1) {
                if (rule.isUnderline()) {
                    setUnderlineDecoration(spannable, rule, startIndex, rule.subWord.length)
                } else {
                    setColorDecoration(spannable, rule, startIndex, rule.subWord.length)
                }
            }
        }
    }
    return spannable
}


private fun getRulesArray(readingWord: ReadingWord): ArrayList<ReadingRule> {
    val rulesList = ArrayList<ReadingRule>()
    //rules example: pink o; white h; book blue oo
    val rules = readingWord.rules.split(";")
    rules.forEach {
        val rule = it.split(" ")
        if (rule.size == 2) { //example: pink o - for words
            rulesList.add(ReadingRule(readingWord.word, rule[1], rule[0]))
        } else if (rule.size == 3) { //book blue oo - for sentences
            rulesList.add(ReadingRule(rule[0], rule[2], rule[1]))
        }
    }
    return rulesList
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
    rule: ReadingRule,
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