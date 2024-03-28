package com.github.rezita.homelearning.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.databinding.ItemFillInSentenceBinding
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.utils.getWithResult
import com.github.rezita.homelearning.utils.getForDisplay
import com.github.rezita.homelearning.utils.getSeparatorIndex

class SentenceAdapter(
    val onItemValueChange: ((Int, String) -> Unit)
) : RecyclerView.Adapter<SentenceAdapter.ListItemHolder>() {
    private var sentences: List<FillInSentence> = emptyList()
    private var isChecked: Boolean = false

    @SuppressLint("NotifyDataSetChanged")
    fun loadSentences(sentences: List<FillInSentence>) {
        this.sentences = sentences.toMutableList()
        notifyItemRangeChanged(0, sentences.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChecked(isChecked: Boolean) {
        this.isChecked = isChecked
        notifyItemRangeChanged(0, sentences.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SentenceAdapter.ListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFillInSentenceBinding.inflate(inflater, parent, false)
        return ListItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return sentences.size
    }

    override fun onBindViewHolder(holder: SentenceAdapter.ListItemHolder, position: Int) {
        val index = holder.adapterPosition
        holder.itemBind(sentences[index], index)
    }

    inner class ListItemHolder(val binding: ItemFillInSentenceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var sentenceText = binding.irregularSentenceText
        private var resultText = binding.irregularResultText

        init {
            binding.root.isClickable = false
            sentenceText.isFocusable = true
            sentenceText.isFocusableInTouchMode = true
        }

        fun itemBind(fillInSentence: FillInSentence, index: Int) {
            val indexSting = "${index + 1}.) "

            when (isChecked) {
                false -> {
                    resultText.visibility = View.GONE
                    sentenceText.visibility = View.VISIBLE
                    sentenceText.removeTextChangedListener(textWatcher)
                    sentenceText.setEditableText(fillInSentence.answer)
                    val baseText = indexSting + fillInSentence.getForDisplay()

                    val startIndex =
                        fillInSentence.getSeparatorIndex() + indexSting.length
                    sentenceText.setBaseText(baseText, startIndex)
                    sentenceText.addTextChangedListener(textWatcher)
                }

                else -> {
                    resultText.visibility = View.VISIBLE
                    sentenceText.visibility = View.GONE
                    resultText.setText(
                        fillInSentence.getWithResult().insert(0, indexSting),
                        TextView.BufferType.NORMAL
                    )
                }
            }
        }

        private val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val answer = sentenceText.editableText
                onItemValueChange(adapterPosition, answer)
                notifyItemChanged(adapterPosition)
            }
        }
    }
}