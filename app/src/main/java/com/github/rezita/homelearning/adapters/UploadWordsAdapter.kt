package com.github.rezita.homelearning.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.ItemUploadSpellingWordsBinding
import com.github.rezita.homelearning.model.SpellingWord

abstract class UploadWordsListItemHolder(val binding: ItemUploadSpellingWordsBinding) :
    RecyclerView.ViewHolder(binding.root)

class UploadWordsAdapter(
    val context: Context,
    private val selectionDeleteInteraction: (Int) -> Unit,
    private val selectionChangeInteraction: (Int, SpellingWord) -> Unit,
) : RecyclerView.Adapter<UploadWordsListItemHolder>() {

    private val _typeHeader = 0
    private val _typeItem = 1

    private var words = mutableListOf<SpellingWord>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadWords(words: List<SpellingWord>) {
        this.words = words.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadWordsListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUploadSpellingWordsBinding.inflate(inflater, parent, false)
        return if (viewType == _typeHeader) {
            ListHeaderRow(binding)
        } else {
            ListItemHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return words.size + 1
    }

    override fun onBindViewHolder(holder: UploadWordsListItemHolder, position: Int) {
        if (holder is ListItemHolder) {
            holder.itemBind(getItem(position))
        } else if (holder is ListHeaderRow) {
            holder.itemBind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) _typeHeader else _typeItem
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    private fun getItem(position: Int): SpellingWord {
        return words[position - 1]
    }


    inner class ListItemHolder(binding: ItemUploadSpellingWordsBinding) :
        UploadWordsListItemHolder(binding) {
        internal val word = binding.uploadWordText
        private val category = binding.uploadCategoryText
        private val comment = binding.uploadCommentText
        private val deleteBtn = binding.uploadDeleteBtn
        private val editBtn = binding.uploadEditBtn

        fun itemBind(spellingWord: SpellingWord) {
            word.text = spellingWord.word
            comment.text = spellingWord.comment
            category.text = spellingWord.category
            setButtonListeners(spellingWord)
        }

        private fun setButtonListeners(spellingWord: SpellingWord) {
            deleteBtn.setOnClickListener { confirmRemoveWord() }
            editBtn.setOnClickListener { editWord(spellingWord) }
        }

        private fun confirmRemoveWord() {
            selectionDeleteInteraction(adapterPosition - 1)
        }

        private fun editWord(spellingWord: SpellingWord) {
            selectionChangeInteraction(adapterPosition - 1, spellingWord)
        }
    }

    inner class ListHeaderRow(binding: ItemUploadSpellingWordsBinding) :
        UploadWordsListItemHolder(binding) {
        internal var word = binding.uploadWordText
        private val category = binding.uploadCategoryText
        private val comment = binding.uploadCommentText
        private val deleteBtn = binding.uploadDeleteBtn
        private val editBtn = binding.uploadEditBtn
        fun itemBind() {
            deleteBtn.visibility = View.GONE
            editBtn.visibility = View.GONE
            word.text = context.getString(R.string.header_upload_words_word)
            comment.text = context.getString(R.string.header_upload_words_comment)
            category.text = context.getString(R.string.header_upload_words_category)
        }
    }
}