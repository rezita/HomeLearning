package com.github.rezita.homelearning.model

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.annotation.VisibleForTesting
import com.github.rezita.homelearning.utils.toListBySeparator

const val RULES_SEPARATOR = ";"
fun ApiReadingWord.asReadingWord() = ReadingWord(
    word = word,
    category = category,
    comment = comment,
    rules = getRules(word, rule)
)

@VisibleForTesting
internal fun getRules(word: String, ruleSource: String): ArrayList<ReadingRule> {
    val rulesList = ArrayList<ReadingRule>()
    //rules example: pink o; white h; book blue oo
    val rules = ruleSource.toListBySeparator(RULES_SEPARATOR)
    rules.forEach {
        val rule = it.split(" ")
        if (rule.size == 2) { //example: pink o - for words
            rulesList.add(ReadingRule(word, rule[1], rule[0]))
        } else if (rule.size == 3) { //book blue oo - for sentences
            rulesList.add(ReadingRule(rule[0], rule[2], rule[1]))
        }
    }
    return rulesList
}