package com.github.rezita.homelearning.network

import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.R
import org.json.JSONArray

class WordsProvider(val context: Context) {

    private val urlBuilder = URLBuilder()
    private val _shortTimeout = 60000   //1 min
    private val _mediumTimeout = 120000   //2 min
    private val _longTimeout = 300000  //5 min

    fun loadSpellingWords(sheetAction: SheetAction, backUpCall: (String) -> Unit){
        loadData(backUpCall, sheetAction)
    }

    fun loadSpellingCategories(sheetAction: SheetAction, backUpCall: (String) -> Unit) {
        loadData(backUpCall, sheetAction)
    }

    fun loadReadingWords(sheetAction: SheetAction, backUpCall: (String) -> Unit) {
        loadData(backUpCall, sheetAction)
    }

    fun loadFillInSentences(sheetAction: SheetAction, backUpCall: (String) -> Unit) {
        loadData(backUpCall, sheetAction)
    }

    fun updateSpellingWords(backupCall: (String) -> Unit, wordsToUpdate: List<SpellingWord>, sheetAction: SheetAction){
        val wordsParam = wordListToMapOfJSON(wordsToUpdate)
        updateData(backupCall, sheetAction, _mediumTimeout,  wordsParam)
    }

    fun updateFillInSentences(backupCall: (String) -> Unit, sentencesToUpdate: List<FillInSentence>, sheetAction: SheetAction){
        val sentenceParam = sentenceListToMapOfJSON(sentencesToUpdate)
        //Log.i("json", wordsParam.toString())
        updateData(backupCall, sheetAction, _mediumTimeout,  sentenceParam)
    }

    fun saveNewSpellingWords(backupCall: (String) -> Unit, wordsToSave: List<SpellingWord>) {
        val wordsParam = wordListToMapOfJSON(wordsToSave)
        updateData(backupCall, SheetAction.SAVE_WORDS, _mediumTimeout, wordsParam)
    }

    fun restoreSpellingWordsFromLogs(backupCall: (String) -> Unit){
        updateData(backupCall, SheetAction.RESTORE_SPELLING_FROM_LOG, _longTimeout)
    }

    private fun loadData(wordsReceiver: (String) -> Unit, action: SheetAction, retryTimeout: Int = _shortTimeout){
        val queue = Volley.newRequestQueue(context)
        val url = urlBuilder.getGetURLAddress(action, getUserName(context))

        val volleyRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                //Log.i("info Response", response.toString())
                wordsReceiver(response.toString())
            }
        ) { error ->
            Log.i("error info", error.toString())
        }

        val policy: RetryPolicy =
            DefaultRetryPolicy(retryTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        volleyRequest.retryPolicy = policy

        queue.add(volleyRequest)
    }

    private fun updateData(wordsReceiver: (String) -> Unit, action: SheetAction, retryTimeout: Int, extras: Map<String, String> = HashMap<String, String>()){
        val queue = Volley.newRequestQueue(context)
        val url = urlBuilder.getPostURLAddress()

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                wordsReceiver(response.toString())
            },
            { error ->
                Log.i("API", "error => $error")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return getPostParams(action, extras)
            }
        }
        val retryPolicy: RetryPolicy =
            DefaultRetryPolicy(retryTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = retryPolicy

        queue.add(stringRequest)
    }


    private fun getPostParams(action: SheetAction, extras: Map<String, String>): Map<String, String> {
        return mapOf("action" to action.value,
            "username" to getUserName(context).toString(),
            "ssId" to urlBuilder.getSheetID()) + extras
    }

    private fun getUserName(context: Context): String? {
        val prefs = context.getSharedPreferences("", Context.MODE_PRIVATE)
        return prefs.getString(R.string.preferences_user_name.toString(), "")
    }

    private fun wordListToMapOfJSON(words: List<SpellingWord>) : Map<String, String>{
        return mapOf("words" to JSONArray(words.map { w-> w.convertToJSON() }).toString())
    }

    private fun sentenceListToMapOfJSON(words: List<FillInSentence>) : Map<String, String>{
        return mapOf("sentences" to JSONArray(words.map { w-> w.convertToJSON() }).toString())
    }
}