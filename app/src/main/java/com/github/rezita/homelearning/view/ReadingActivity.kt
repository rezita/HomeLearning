package com.github.rezita.homelearning.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Either
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.ReadingAdapter
import com.github.rezita.homelearning.databinding.ActivityReadingBinding
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.network.WordsProvider
import com.github.rezita.homelearning.utils.JSONSerializer
import com.github.rezita.homelearning.utils.RemoteError

class ReadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadingBinding
    private val readingWords = ArrayList<ReadingWord>()
    private var wordsProvider: WordsProvider? = null
    private var recyclerView: RecyclerView? = null
    private var wordsAdapter: ReadingAdapter? = null
    private var sheetAction: SheetAction = SheetAction.READ_READING_WORDS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordsProvider = WordsProvider(applicationContext)

        val action: String = intent.getStringExtra("action") ?: SheetAction.READ_READING_WORDS.value
        sheetAction = SheetAction.forValue(action)!!
        binding = ActivityReadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareView()
        setRecyclerView()
        loadReadingWords()

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)

    }

    private fun setRecyclerView(){
        wordsAdapter = ReadingAdapter(this, readingWords)
        recyclerView = binding.readingWordsContainer
        recyclerView!!.adapter = wordsAdapter
    }

    private fun prepareView(){
        binding.readingProgressbar.root.visibility = View.VISIBLE
        binding.readingWordsContainer.visibility = View.GONE
    }

    private fun loadReadingWords(){
        wordsProvider?.loadReadingWords(sheetAction) {onWordsReceived(it) }
    }

    private fun onWordsReceived(response: String){
        parseItems(response).fold(
            { e -> Log.e("error", e.message) },
            { words -> readingWords.addAll(words)}
        )
        updateViewAfterLoading()
    }

    private fun parseItems(jsonResponses: String): Either<RemoteError, ArrayList<ReadingWord>> {
        return JSONSerializer().parseReadingWords(jsonResponses)
    }

    private fun updateViewAfterLoading(){
        if (readingWords.size == 0) {
            //error when loading
            binding.readingProgressbar.root.visibility = View.GONE
            Toast.makeText(this, getString(R.string.loading_fail_text), Toast.LENGTH_SHORT).show()

        } else {
            binding.readingProgressbar.root.visibility = View.GONE
            //update recycler view
            binding.readingWordsContainer.visibility = View.VISIBLE
        }
    }
}