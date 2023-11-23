package com.github.rezita.homelearning.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.ReadingWord

class ReadingAdapter(val context: Context,
                     private val wordList: List<ReadingWord>)
    : RecyclerView.Adapter<ReadingAdapter.ListItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingAdapter.ListItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reading, parent,false)
        return ListItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onBindViewHolder(holder: ReadingAdapter.ListItemHolder, position: Int) {
        holder.itemBind(wordList[position], position)
    }

    inner class ListItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private var wordText = view.findViewById<TextView>(R.id.reading_word_text)
        private var counter = view.findViewById<TextView>(R.id.reading_word_counter)

        fun itemBind(readingWord: ReadingWord, position: Int){
            wordText.text = readingWord.getDecorated()
            counter.text = this@ReadingAdapter.context.getString(R.string.reading_counter, position + 1, itemCount)
        }
    }
}