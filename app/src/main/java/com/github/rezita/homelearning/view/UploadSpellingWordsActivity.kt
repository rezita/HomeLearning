package com.github.rezita.homelearning.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Either
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.UploadWordsAdapter
import com.github.rezita.homelearning.databinding.ActivityUploadSpellingWordsBinding
import com.github.rezita.homelearning.dialogs.DialogNewSpellingWord
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.network.WordsProvider
import com.github.rezita.homelearning.utils.JSONSerializer
import com.github.rezita.homelearning.utils.RemoteError

class UploadSpellingWordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadSpellingWordsBinding

    private var categories: ArrayList<String> = ArrayList()
    private var words: ArrayList<SpellingWord> = ArrayList()
    private var wordsProvider: WordsProvider? = null
    private var recyclerView: RecyclerView? = null
    private var wordsAdapter: UploadWordsAdapter? = null
    private val _maxSizeOfWords = 10
    private val _responseWordSeparator = ", "
    private val _responseWordMessageSeparator = ":"

    private var sheetAction: SheetAction = SheetAction.SAVE_ERIK_WORDS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val action: String = intent.getStringExtra("action") ?: SheetAction.SAVE_ERIK_WORDS.value
        sheetAction = SheetAction.forValue(action)!!

        binding = ActivityUploadSpellingWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerView()
        wordsProvider = WordsProvider(applicationContext)
        loadCategoriesAndPrepareView()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.spelling_menu, menu)
        menu?.findItem(R.id.menu_add_new)?.isVisible = (words.size < _maxSizeOfWords)
        menu?.findItem(R.id.menu_save)?.isVisible = (words.isNotEmpty())
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_add_new)?.isVisible = (words.size < _maxSizeOfWords)
        menu?.findItem(R.id.menu_save)?.isVisible = (words.isNotEmpty())
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_new -> {
                createNewSpellingWord()
                true
            }
            R.id.menu_save -> {
                saveNewWords()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setRecyclerView() {
        val manager = LinearLayoutManager(this)
        recyclerView = binding.uploadWordsContainer
        recyclerView!!.layoutManager = manager
        recyclerView!!.setHasFixedSize(true)
        wordsAdapter = UploadWordsAdapter(
            this,
            { position -> onItemRemove(position) },
            { position, wordText, comment, category ->
                onItemChange(
                    position,
                    wordText,
                    comment,
                    category
                )
            },
            words, categories, getCategoryAction()
        )
        //divider line
        recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = wordsAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onItemRemove(position: Int) {
        words.removeAt(position - 1)
        updateLayout()
        wordsAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onItemChange(position: Int, wordText: String, comment: String, category: String) {
        //words.removeAt(position - 1)
        words[position - 1].changeWord(wordText, comment, category)
        //updateLayout()
        wordsAdapter?.notifyDataSetChanged()
    }

    private fun loadCategoriesAndPrepareView() {
        updateLayout()
        setPrBarVisibility(true)
        wordsProvider?.loadSpellingCategories(getCategoryAction()) { categories -> onCategoriesReceived(categories) }
    }

    private fun saveNewWords() {
        setInfoTextProperties(getString(R.string.info_uploading))
        setPrBarVisibility(true)
        wordsProvider?.saveNewSpellingWords({ response -> onWordsSaved(response) }, words, sheetAction)
    }

    private fun onWordsSaved(response: String) {
        setPrBarVisibility(false)
        showSavingResponse(response)
    }

    private fun onCategoriesReceived(response: String) {
        setPrBarVisibility(false)
        parseCategories(response).fold(
            { e -> Log.e("error", e.message) },
            { categories -> this.categories = categories }
        )
        updateLayout()
    }

    private fun parseCategories(jsonResponses: String): Either<RemoteError, ArrayList<String>> {
        return JSONSerializer().parseCategories(jsonResponses)
    }

    private fun showSavingResponse(response: String) = AlertDialog.Builder(this)
        .setMessage(createSavingMessage(response))
        .setPositiveButton(getString(R.string.ok)) { _, _ -> clearWordsList() }
        .create()
        .show()

    private fun createSavingMessage(response: String): String {
        val responses = parseUploadResponse(response)
        var message = getString(R.string.saving_words_response)
        responses.forEach { (wordResponse, wordResult) -> message += "${wordResponse}: $wordResult \n" }
        return message
    }

    private fun parseUploadResponse(response: String): ArrayList<ArrayList<String>> {
        val result = ArrayList<ArrayList<String>>()
        val responses = response.removeSuffix(_responseWordSeparator).split(_responseWordSeparator)
        responses.forEach { wordResponse ->
            result.add(
                wordResponse.split(
                    _responseWordMessageSeparator
                ) as ArrayList<String>
            )
        }
        return result
    }

    private fun createNewSpellingWord() {
        if (wordsProvider == null) {
            wordsProvider = WordsProvider(applicationContext)
        }

        val dialog = DialogNewSpellingWord(
            wordsProvider!!, getCategoryAction(),
            { word -> addWordToWords(this, word) },
            categories
        )

        dialog.show(supportFragmentManager, "DialogNewQuestion")
    }

    private fun updateLayout() {
        setPrBarVisibility(false)
        invalidateOptionsMenu()
        if (words.isEmpty()) {
            recyclerView?.visibility = View.GONE
            setInfoTextProperties(getString(R.string.info_no_words_upload))
            binding.uploadWordsInfoLayout.root.visibility = View.VISIBLE

        } else {
            recyclerView?.visibility = View.VISIBLE
            setInfoTextProperties(getString(R.string.info_words_to_upload))
        }
    }

    private fun setPrBarVisibility(isVisible: Boolean) {
        when (isVisible) {
            true -> binding.uploadWordsProgressbar.root.visibility = View.VISIBLE
            false -> binding.uploadWordsProgressbar.root.visibility = View.GONE
        }
    }

    private fun clearWordsList() {
        words.clear()
        updateLayout()
    }

    companion object {
        @SuppressLint("NotifyDataSetChanged")
        private fun addWordToWords(
            uploadSpellingWordsActivity: UploadSpellingWordsActivity,
            word: SpellingWord
        ) {
            uploadSpellingWordsActivity.words.add(word)
            uploadSpellingWordsActivity.updateLayout()
            uploadSpellingWordsActivity.wordsAdapter?.notifyDataSetChanged()
        }
    }

    private fun setInfoTextProperties(text: String) {
        binding.uploadWordsInfoLayout.infoText.text = text
    }

    private fun getCategoryAction(): SheetAction{
        return when (sheetAction) {
            SheetAction.SAVE_ERIK_WORDS -> SheetAction.READ_ERIK_SPELLING_CATEGORIES
            else -> SheetAction.READ_MARK_SPELLING_CATEGORIES
        }
    }
}
