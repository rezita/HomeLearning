package com.github.rezita.homelearning.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Either
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.adapters.SentenceAdapter
import com.github.rezita.homelearning.databinding.ActivityFillInSentencesBinding
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.network.WordsProvider
import com.github.rezita.homelearning.utils.JSONSerializer
import com.github.rezita.homelearning.utils.RemoteError


class FillInSentencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFillInSentencesBinding
    private val fillInSentences = ArrayList<FillInSentence>()
    private var wordsProvider: WordsProvider? = null

    private var recyclerView: RecyclerView? = null
    private var sentencesAdapter: SentenceAdapter? = null

    private var sheetAction: SheetAction = SheetAction.READ_IRREGULAR_VERBS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordsProvider = WordsProvider(applicationContext)

        val action: String =
            intent.getStringExtra("action") ?: SheetAction.READ_IRREGULAR_VERBS.value
        sheetAction = SheetAction.forValue(action)!!

        binding = ActivityFillInSentencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareView()
        loadSentences()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.irregular_verb_menu, menu)
        menu?.findItem(R.id.menu_check)?.isVisible = isCheckable()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_check)?.isVisible = isCheckable()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_check -> {
                checkAndUploadResults()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isCheckable(): Boolean {
        val resultText = binding.irregularInfoLayout.scoreText
        val progressBar = binding.sentencesProgressbar.root
        return (resultText.visibility == View.GONE && progressBar.visibility == View.GONE)
    }

    private fun setRecyclerView() {
        sentencesAdapter =
            SentenceAdapter(
                this,
                fillInSentences
            )
        recyclerView = binding.sentencesContainer
        recyclerView!!.adapter = sentencesAdapter
    }

    private fun loadSentences() {
        setPrBarVisibility(true)
        wordsProvider?.loadFillInSentences(sheetAction) { onVerbsReceived(it) }
    }

    private fun onVerbsReceived(response: String) {
        setPrBarVisibility(false)
        parseItems(response).fold(
            { e -> Log.e("error", e.message) },
            { fillInSentences.addAll(it) }
        )
        updateViewAfterLoading()
    }

    private fun parseItems(jsonResponses: String): Either<RemoteError, ArrayList<FillInSentence>> {
        return JSONSerializer().parseSentences(jsonResponses)
    }

    private fun prepareView() {
        setPrBarVisibility(false)
        binding.sentencesContainer.visibility = View.GONE
        setInfoTextProperties(getString(R.string.loading_data_text))
        binding.irregularInfoLayout.scoreText.visibility = View.GONE

    }

    private fun updateViewAfterLoading() {
        setPrBarVisibility(false)
        if (fillInSentences.size == 0) {
            //error when loading
            setInfoTextProperties(getString(R.string.loading_fail_text))
            binding.sentencesContainer.visibility = View.GONE
            //Toast.makeText(this, getString(R.string.loading_fail_text), Toast.LENGTH_SHORT).show()
        } else {
            setRecyclerView()
            setInfoTextProperties(getString(R.string.irregular_verb_instruction))
            binding.sentencesContainer.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkAndUploadResults() {
        if (!isAllAnswered()) {
            showNotAllAnsweredDialog()
        } else {
            hideKeyboard()
            updateAndSaveResults()
        }
    }

    private fun updateAndSaveResults() {
        setPrBarVisibility(true)
        setInfoTextProperties(getString(R.string.irregular_checking_text))
        updateAnswers()
        saveFillInSentenceResults()
    }

    private fun updateAnswers() {
        //get answers and update sentences
        for (index in 0 until fillInSentences.size) {
            val answer = sentencesAdapter!!.getAnswer(index)
            val currentSentence = fillInSentences[index]
            fillInSentences[index] = getUpdatedSentence(answer, currentSentence)
            sentencesAdapter!!.notifyItemChanged(index)
        }
    }

    private fun getUpdatedSentence(answer: String, sentence: FillInSentence): FillInSentence {
        val status = when (answer) {
            "" -> WordStatus.UNCHECKED
            in sentence.solutions -> WordStatus.CORRECT
            else -> WordStatus.INCORRECT
        }
        return sentence.copy(answer = answer, status = status)
    }

    private fun saveFillInSentenceResults() {
        val action = when (sheetAction) {
            SheetAction.READ_IRREGULAR_VERBS -> SheetAction.UPDATE_IRREGULAR_VERBS
            else -> SheetAction.UPDATE_HOMOPHONES
        }
        wordsProvider?.updateFillInSentences({ onWordsUpdated(it) }, fillInSentences, action)
    }

    private fun onWordsUpdated(response: String) {
        setPrBarVisibility(false)
        setInfoTextProperties("")
        updateScores()
        invalidateOptionsMenu()
        Log.i("response", response)
        Toast.makeText(this, R.string.sentences_message_after_update, Toast.LENGTH_SHORT).show()
    }

    private fun isAllAnswered(): Boolean {
        return sentencesAdapter!!.isAllAnswered()
    }

    private fun showNotAllAnsweredDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.irregular_verb_not_all_filled_message))
            .setNeutralButton(getString(R.string.ok)) { dialogInterface, _ -> dialogInterface.cancel() }
            .setTitle(getString(R.string.irregular_verb_dialog_title))
            .create()
            .show()
    }

    @SuppressLint("StringFormatInvalid")
    private fun updateScores() {
        val resultText = binding.irregularInfoLayout.scoreText

        val answeredQuestions = fillInSentences.filter { word -> word.isChanged() }.size
        val correctAnswers =
            fillInSentences.filter { word -> word.status == WordStatus.CORRECT }.size
        val resultRatio = correctAnswers * 100 / answeredQuestions
        val scoreString = getString(
            R.string.irregular_verb_result,
            correctAnswers,
            answeredQuestions,
            resultRatio
        )
        //binding.scoreTex.text = "Result: ${correctAnswers} / ${answeredQuestions} ( ${result}%)"
        resultText.text = scoreString
        resultText.visibility = View.VISIBLE
    }

    private fun setInfoTextProperties(text: String) {
        binding.irregularInfoLayout.infoText.text = text
    }

    private fun setPrBarVisibility(isVisible: Boolean) {
        when (isVisible) {
            true -> binding.sentencesProgressbar.root.visibility = View.VISIBLE
            false -> binding.sentencesProgressbar.root.visibility = View.GONE
        }
        invalidateOptionsMenu()
    }

    private fun hideKeyboard() {
        //edittext.onEditorAction(EditorInfo.IME_ACTION_DONE);
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }
}