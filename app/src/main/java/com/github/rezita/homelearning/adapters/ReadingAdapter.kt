package com.github.rezita.homelearning.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.ItemReadingBinding
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.utils.getForDisplay

class ReadingAdapter : RecyclerView.Adapter<ReadingAdapter.ListItemHolder>() {
    private var words = mutableListOf<ReadingWord>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadWords(words: List<ReadingWord>) {
        this.words = words.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReadingBinding.inflate(inflater, parent, false)
        return ListItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val word = words[position]
        holder.binding.readingWordText.text = word.getForDisplay()
        val index =
            holder.itemView.context.getString(R.string.reading_counter, position + 1, itemCount)
        holder.binding.readingWordCounter.text = index
    }

    inner class ListItemHolder(val binding: ItemReadingBinding) :
        RecyclerView.ViewHolder(binding.root)

}