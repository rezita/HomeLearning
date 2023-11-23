package com.github.rezita.homelearning.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus

class SpellingWordAdapter(
    val context: Context,
    private val selectionInteraction: (Int, WordStatus) -> Unit,
    private val wordList: List<SpellingWord>)
    : RecyclerView.Adapter<SpellingWordAdapter.ListItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_spelling, parent,false)
        return ListItemHolder(itemView, selectionInteraction)
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        holder.itemBind(wordList[position])
    }

    inner class ListItemHolder(view: View, private val selectionInteraction: (Int, WordStatus) -> Unit)
        : RecyclerView.ViewHolder(view) {
        internal var word = view.findViewById<TextView>(R.id.spelling_word_text)
        private var resultGroup = view.findViewById<RadioGroup>(R.id.spelling_word_result)
        private var positiveResult = view.findViewById<RadioButton>(R.id.positive_result)
        private var negativeResult = view.findViewById<RadioButton>(R.id.negative_result)

        init{
            view.isClickable = true
        }

        fun itemBind(spellingWord: SpellingWord){
            word.text = spellingWord.word
            setRadioButtons(spellingWord.status)
            setRadioButtonListeners()
        }

        private fun setRadioButtons(status: WordStatus) {
            resultGroup.clearCheck()

            when (status) {
                WordStatus.UNCHECKED -> return
                WordStatus.INCORRECT ->  resultGroup.check(negativeResult.id)
                WordStatus.CORRECT -> resultGroup.check(positiveResult.id)
            }
        }

        private fun setRadioButtonListeners(){
            positiveResult.setOnClickListener{(radioButtonOnClicked(WordStatus.CORRECT))}
            negativeResult.setOnClickListener{(radioButtonOnClicked(WordStatus.INCORRECT))}
        }

        private fun radioButtonOnClicked(value: WordStatus){
            selectionInteraction(adapterPosition, value)
            //wordList[adapterPosition].status = value
        }
    }
}