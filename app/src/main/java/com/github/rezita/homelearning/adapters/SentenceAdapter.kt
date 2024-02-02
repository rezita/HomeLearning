package com.github.rezita.homelearning.adapters

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.SEPARATOR
import com.github.rezita.homelearning.model.SOLUTION_SEPARATOR
import com.github.rezita.homelearning.model.SPACES
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.utils.indexOfOrZero
import com.github.rezita.homelearning.utils.joinWithSeparator
import com.github.rezita.partiallyeditabletext.PartiallyEditableText

class SentenceAdapter(
    val context: Context,
    private val sentenceList: List<FillInSentence>
) : RecyclerView.Adapter<SentenceAdapter.ListItemHolder>() {

    private var answers: Array<String> = Array(sentenceList.size){""}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentenceAdapter.ListItemHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_fill_in_sentence, parent, false)
        return ListItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return sentenceList.size
    }

    override fun onBindViewHolder(holder: SentenceAdapter.ListItemHolder, position: Int) {
        holder.itemBind(sentenceList[position], position)
    }

    fun isAllAnswered(): Boolean {
        return answers.all { it.isNotEmpty() }
    }

    fun getAnswer(index: Int): String {
        return answers[index]
    }

    fun setAnswer(index: Int, value: String){
        answers[index] = value
    }

    inner class ListItemHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private var sentenceText = view.findViewById<PartiallyEditableText>(R.id.irregular_sentence_text)
        private var resultText = view.findViewById<TextView>(R.id.irregular_result_text)

        init {
            view.isClickable = false
            sentenceText.isFocusable = true
            sentenceText.isFocusableInTouchMode = true
        }

        fun itemBind(fillInSentence: FillInSentence, index: Int) {
            val status = fillInSentence.status
            val indexSting = "${index + 1}.) "

            setListeners(index)

            when (status) {
                WordStatus.UNCHECKED -> {
                    resultText.visibility = View.GONE
                    sentenceText.visibility = View.VISIBLE
                    val baseText = indexSting + fillInSentence.getSentenceForDisplay()
                    val startIndex = fillInSentence.sentence.indexOfOrZero(SEPARATOR) + indexSting.length
                    sentenceText.setBaseText(baseText, startIndex)
                }
                else -> {
                    resultText.visibility = View.VISIBLE
                    sentenceText.visibility = View.GONE
                    resultText.setText(fillInSentence.getAsResult().insert(0, indexSting), TextView.BufferType.NORMAL)
                }
            }
        }

        private fun setListeners(index: Int){
            sentenceText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val answer = sentenceText.editableText
                    setAnswer(index, answer)
                }
            })
        }
    }
}

internal fun FillInSentence.getWithSuggestionsAndSeparatorReplaced(replacement: String): String{
    val sentenceReplaced = sentence.replace(SEPARATOR, replacement)
    return "$sentenceReplaced ($suggestion)"
}

fun FillInSentence.getSentenceForDisplay(): String {
    return getWithSuggestionsAndSeparatorReplaced(SPACES)
}

fun FillInSentence.getAsResult(): SpannableStringBuilder {
    val separatorIndex = sentence.indexOfOrZero(SEPARATOR)
    val result = SpannableStringBuilder(getWithSuggestionsAndSeparatorReplaced(""))
    val solutionsString = solutions.joinWithSeparator(SOLUTION_SEPARATOR)
    when (status) {
        WordStatus.UNCHECKED -> {
            result.insert(separatorIndex, solutionsString)
            result.setSpan(
                ForegroundColorSpan(Color.RED),
                separatorIndex,
                separatorIndex + solutionsString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        WordStatus.CORRECT -> {
            result.insert(separatorIndex, answer)
            result.setSpan(
                ForegroundColorSpan(Color.GREEN),
                separatorIndex,
                separatorIndex + answer.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        WordStatus.INCORRECT -> {
            result.insert(separatorIndex, answer)
            result.setSpan(
                ForegroundColorSpan(Color.RED),
                separatorIndex,
                separatorIndex + answer.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            result.setSpan(
                StrikethroughSpan(),
                separatorIndex,
                separatorIndex + answer.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            //add extra space between answer and solutions
            result.insert(separatorIndex + answer.length, " ")

            result.insert(separatorIndex + answer.length + 1, solutionsString)
            result.setSpan(
                ForegroundColorSpan(Color.GREEN),
                separatorIndex + answer.length + 1,
                separatorIndex + answer.length + solutionsString.length + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    return result
}
