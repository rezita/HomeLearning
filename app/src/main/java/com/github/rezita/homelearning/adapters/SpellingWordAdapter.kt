package com.github.rezita.homelearning.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.databinding.ItemSpellingBinding
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus

class SpellingWordAdapter(
    private val selectionInteraction: (Int, WordStatus) -> Unit
) : RecyclerView.Adapter<SpellingWordAdapter.ListItemHolder>() {
    private var words = mutableListOf<SpellingWord>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadWords(words: List<SpellingWord>) {
        this.words = words.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpellingWordAdapter.ListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSpellingBinding.inflate(inflater, parent, false)
        return ListItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val index = holder.adapterPosition
        holder.binding.spellingWordText.text = words[index].word
        setRadioButtons(holder, index)
    }

    private fun setRadioButtons(holder: ListItemHolder, index: Int) {
        holder.itemBind(words[index])
    }

    inner class ListItemHolder(val binding: ItemSpellingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val word = binding.spellingWordText
        private val resultGroup = binding.spellingWordResult
        private val negativeResult = binding.negativeResult
        private val positiveResult = binding.positiveResult

        fun itemBind(spellingWord: SpellingWord) {
            word.text = spellingWord.word
            setRadioButtons(spellingWord.status)
            setRadioButtonListeners()
        }

        private fun setRadioButtons(status: WordStatus) {
            resultGroup.clearCheck()
            when (status) {
                WordStatus.UNCHECKED -> return
                WordStatus.INCORRECT -> resultGroup.check(negativeResult.id)
                WordStatus.CORRECT -> resultGroup.check(positiveResult.id)
            }
        }

        private fun setRadioButtonListeners() {
            positiveResult.setOnClickListener { (radioButtonOnClicked(WordStatus.CORRECT)) }
            negativeResult.setOnClickListener { (radioButtonOnClicked(WordStatus.INCORRECT)) }
        }

        private fun radioButtonOnClicked(value: WordStatus) {
            selectionInteraction(adapterPosition, value)
        }
    }
}