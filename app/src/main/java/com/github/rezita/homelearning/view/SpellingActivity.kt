package com.github.rezita.homelearning.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Either
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.SpellingWordAdapter
import com.github.rezita.homelearning.databinding.ActivitySpellingBinding
import com.github.rezita.homelearning.dialogs.DialogNewSpellingWord
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.network.WordsProvider
import com.github.rezita.homelearning.utils.JSONSerializer
import com.github.rezita.homelearning.utils.RemoteError

class SpellingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpellingBinding
    private val spellingWords = ArrayList<SpellingWord>()
    private var recyclerView: RecyclerView? = null
    private var wordsAdapter: SpellingWordAdapter? = null
    private var wordsProvider: WordsProvider? = null

    private var sheetAction: SheetAction = SheetAction.READ_ERIK_SPELLING_WORDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordsProvider = WordsProvider(applicationContext)

        val action: String = intent.getStringExtra("action") ?: SheetAction.READ_ERIK_SPELLING_WORDS.value
        sheetAction = SheetAction.forValue(action)!!

        binding = ActivitySpellingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareView()
        setRecyclerView()
        loadSpellingWords()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.spelling_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_new-> {
                createNewSpellingWord()
                true
            }
            R.id.menu_save-> {
                uploadSpellingWords()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setRecyclerView() {
        recyclerView = binding.spellingWordsContainer
        wordsAdapter = SpellingWordAdapter(this, { position, status -> onItemSelected(position, status)},  spellingWords)

        //divider line
        recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = wordsAdapter
    }

    private fun onWordsReceived(response: String) {
        //Log.i("wtf", "thread: ${Thread.currentThread().name}, id: ${Thread.currentThread().id}")
        //process the data with the JSON Serializer
        setPrBarVisibility(false)
        parseItems(response).fold(
            { e -> Log.e("error", e.message) },
            { spellingWords.addAll(it)}
        )
        updateViewAfterLoading()
    }

    private fun parseItems(jsonResponses: String): Either<RemoteError, ArrayList<SpellingWord>> {
        return JSONSerializer().parseSpellingWords(jsonResponses)
    }

    private fun loadSpellingWords() {
        //Log.i("thread", "thread: ${Thread.currentThread().name}, id: ${Thread.currentThread().id}")
        setPrBarVisibility(true)
        wordsProvider?.loadSpellingWords(sheetAction) { onWordsReceived(it) }
    }

    private fun uploadSpellingWords() {
        updateViewWhileUpdateWords()
        val action = when(sheetAction){
            SheetAction.READ_ERIK_SPELLING_WORDS -> SheetAction.UPDATE_ERIK_SPELLING_WORDS
            else -> SheetAction.UPDATE_MARK_SPELLING_WORDS
        }
        wordsProvider?.updateSpellingWords({onWordsUpdated(it)}, getChangedWords(), action)
    }

    private fun onWordsUpdated(response: String) {
        //TODO: processing response
        Toast.makeText(this, R.string.message_after_update_spelling_words, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getChangedWords(): List<SpellingWord> {
        return spellingWords.filter { word -> word.isChanged() }
    }

    private fun onItemSelected(position: Int, status: WordStatus) {
        spellingWords[position].changeStatus(status)
        updateScores()
        //change the adapter's list
        this.wordsAdapter?.notifyItemChanged(position)
    }

    private fun createNewSpellingWord(){
        val action = when (sheetAction) {
            SheetAction.READ_ERIK_SPELLING_WORDS -> SheetAction.READ_ERIK_SPELLING_CATEGORIES
            else -> SheetAction.READ_MARK_SPELLING_CATEGORIES
        }

        if (wordsProvider == null) {
            wordsProvider = WordsProvider(applicationContext)
            val dialog = DialogNewSpellingWord (wordsProvider!!, action) { saveNewSpellingWord(it) }
            dialog.show(supportFragmentManager, "DialogNewQuestion")

        } else {
            val dialog = DialogNewSpellingWord (wordsProvider!!, action) { saveNewSpellingWord(it) }
            dialog.show(supportFragmentManager, "DialogNewQuestion")

        }
    }

    private fun saveNewSpellingWord(word: SpellingWord){
        setPrBarVisibility(true)
        setInfoTextProperties(getString(R.string.saving_spelling_word_text), true)
        val action = when(sheetAction){
            SheetAction.READ_ERIK_SPELLING_WORDS -> SheetAction.SAVE_ERIK_WORDS
            else -> SheetAction.SAVE_MARK_WORDS
        }
        wordsProvider?.saveNewSpellingWords({onWordSaved(it)}, listOf(word), action)
    }

    private fun onWordSaved(response: String) {
        setPrBarVisibility(false)
        setInfoTextProperties("", false)
        Toast.makeText(this, "Word saved", Toast.LENGTH_SHORT).show()
        Log.i("saveResponse", response)
    }

    private fun prepareView() {
        binding.spellingWordsContainer.visibility = View.GONE
        setInfoTextProperties(getString(R.string.loading_data_text), true)
        setPrBarVisibility(false)
    }

    private fun updateViewAfterLoading() {
        //loading textview has to be gone
        if (spellingWords.size == 0) {
            //error when loading
            setInfoTextProperties( getString(R.string.loading_fail_text), true)
            binding.spellingWordsContainer.visibility = View.GONE
        } else {
            //update recycler view
            setInfoTextProperties( "", false)
            binding.spellingWordsContainer.visibility = View.VISIBLE
        }
    }

    private fun updateViewWhileUpdateWords(){
        setPrBarVisibility(true)
        setInfoTextProperties(getString(R.string.data_saving_text), true)
    }

    private fun setPrBarVisibility(isVisible : Boolean){
        when(isVisible){
            true -> binding.spellingProgressbar.root.visibility = View.VISIBLE
            false -> binding.spellingProgressbar.root.visibility = View.GONE
        }
    }

    private fun setInfoTextProperties(text: String, isVisible: Boolean){
        binding.spellingInfoLayout.infoText.text = text
        when(isVisible){
            true -> binding.spellingInfoLayout.infoText.visibility = View.VISIBLE
            false -> binding.spellingInfoLayout.infoText.visibility = View.GONE
        }
    }

    @SuppressLint("StringFormatInvalid")
    fun updateScores(){
        binding.spellingInfoLayout.scoreText.visibility = View.VISIBLE
        val answeredQuestions = spellingWords.filter { word -> word.isChanged() }.size
        val correctAnswers = spellingWords.filter { word -> word.status == WordStatus.CORRECT }.size
        val resultRatio = correctAnswers *100 / answeredQuestions
        val scoreString = getString(R.string.spelling_result, correctAnswers, answeredQuestions, resultRatio)
        //binding.scoreTex.text = "Result: ${correctAnswers} / ${answeredQuestions} ( ${result}%)"
        binding.spellingInfoLayout.scoreText.text = scoreString
    }
}