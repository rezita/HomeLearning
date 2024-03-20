package com.github.rezita.homelearning.adapters

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.dialogs.DialogNewSpellingWord
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.network.WordsProvider
import com.github.rezita.homelearning.view.UploadSpellingWordsActivity as UploadSpellingWordsActivity

class UploadWordsAdapter(val context: Context,
                         private val selectionDeleteInteraction: (Int) -> Unit,
                         private val selectionChangeInteraction: (Int, SpellingWord) -> Unit,
                         private val wordList: List<SpellingWord>,
                         private val categories: ArrayList<String>,
                         private val sheetAction: SheetAction)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _typeHeader = 0
    private val _typeItem = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_upload_spelling_words, parent,false)

        return if (viewType == _typeHeader) {
            ListHeaderRow(itemView)
        } else {
            ListItemHolder(itemView, selectionDeleteInteraction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListItemHolder) {
//            holder.itemBind(wordList[position])
            holder.itemBind(getItem(position))
        } else if (holder is ListHeaderRow) {
            holder.itemBind()
        }
    }

    override fun getItemCount(): Int {
        return wordList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) _typeHeader else _typeItem
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    private fun getItem(position: Int): SpellingWord {
        return wordList[position - 1]
    }

    inner class ListItemHolder(view: View, private val selectionInteraction: (Int) -> Unit)
        : RecyclerView.ViewHolder(view) {

        internal var word = view.findViewById<TextView>(R.id.upload_word_text)
        private var category = view.findViewById<TextView>(R.id.upload_category_text)
        private var comment = view.findViewById<TextView>(R.id.upload_comment_text)
        private var deleteBtn = view.findViewById<ImageButton>(R.id.upload_delete_btn)
        private var editBtn = view.findViewById<ImageButton>(R.id.upload_edit_btn)

        init{
            view.isClickable = true
        }

        fun itemBind(spellingWord: SpellingWord){
            word.text = spellingWord.word
            category.text = spellingWord.category
            comment.text =spellingWord.comment
            setButtonListeners(spellingWord)
        }

        private fun setButtonListeners(spellingWord: SpellingWord){
            deleteBtn.setOnClickListener{confirmRemoveWord()}
            editBtn.setOnClickListener{editWord(spellingWord)}
        }

        private fun confirmRemoveWord(){
            AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.confirm_remove_upload_word))
                .setPositiveButton(context.getString(R.string.yes_button), DialogInterface.OnClickListener { _, _ -> (selectionInteraction(adapterPosition))})
                .setNegativeButton(context.getString(R.string.no_button)) { dialogInterface, _ -> dialogInterface.cancel() }
                .create()
                .show()
        }

        private fun editWord(spellingWord: SpellingWord){
            val activity = context as UploadSpellingWordsActivity
            val dialog = DialogNewSpellingWord (WordsProvider(context), sheetAction, { word -> changeWord(word) }, spellingWord, categories)
            dialog.show(activity.supportFragmentManager ,"DialogNewQuestion")
        }

        private fun changeWord(word: SpellingWord){
            selectionChangeInteraction(adapterPosition, word)
        }
    }

    inner class ListHeaderRow(view: View)
        : RecyclerView.ViewHolder(view) {

        internal var word = view.findViewById<TextView>(R.id.upload_word_text)
        private var category = view.findViewById<TextView>(R.id.upload_category_text)
        private var comment = view.findViewById<TextView>(R.id.upload_comment_text)
        private var deleteBtn = view.findViewById<ImageButton>(R.id.upload_delete_btn)
        private var editBtn = view.findViewById<ImageButton>(R.id.upload_edit_btn)

        init{
            view.isClickable = false
        }

        fun itemBind() {
            deleteBtn.visibility = View.GONE
            editBtn.visibility = View.GONE
            word.text = context.getString(R.string.header_upload_words_word)
            category.text = context.getString(R.string.header_upload_words_category)
            comment.text = context.getString(R.string.header_upload_words_comment)
        }
   }
}