package com.github.rezita.homelearning.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
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
                    val baseText = indexSting + fillInSentence.getSentenceWithSpaceAndSuggestion()
                    val startIndex = fillInSentence.getSeparatorIndex() + indexSting.length
                    sentenceText.setBaseText(baseText, startIndex)
                }
                else -> {
                    resultText.visibility = View.VISIBLE
                    sentenceText.visibility = View.GONE
                    resultText.text = fillInSentence.getAsResult().insert(0, indexSting)
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