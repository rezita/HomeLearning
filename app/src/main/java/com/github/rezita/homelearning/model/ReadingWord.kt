package com.github.rezita.homelearning.model

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan

class ReadingWord(val word: String) {
    private var category: String = ""
    private var comment: String = ""
    private var rules: String = ""

    constructor(word: String, category: String, comment: String, rules: String) : this(word) {
        this.category = category
        this.comment = comment
        this.rules = rules
    }

    fun getDecorated(): SpannableString {
        val spannable = SpannableString(word)
        val rules = getRulesArray()
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

    private fun getRulesArray(): ArrayList<ReadingRule> {
        val rulesList = ArrayList<ReadingRule>()
        val rules = rules.split(";")
        rules.forEach {
            val rule = it.split(" ")
            if (rule.size == 2) {
                rulesList.add(ReadingRule(word, rule[1], rule[0]))
            } else if (rule.size == 3) {
                rulesList.add(ReadingRule(rule[0], rule[2], rule[1]))
            }
        }
        return rulesList
    }
}