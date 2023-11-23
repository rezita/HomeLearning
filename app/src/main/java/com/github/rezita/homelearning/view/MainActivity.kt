package com.github.rezita.homelearning.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.ActivityMainBinding
import com.github.rezita.homelearning.network.WordsProvider


@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var userName : String? = null
    private var wordsProvider: WordsProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //removeUserName()
        wordsProvider = WordsProvider(applicationContext)
        setUserName()

        prepareView()
        binding.btnStartSpelling.setOnClickListener{startErikSpelling()}
        binding.btnAddSpellingWords.setOnClickListener{addNewWords()}
        binding.btnStartReading.setOnClickListener { startReading() }
        binding.btnStartIrregular.setOnClickListener { startIrregularVerbs() }
        binding.btnRestoreSpelling.setOnClickListener{confirmRestoreSpellingFromLogs()}
    }

    private fun prepareView(){
        setPrBarVisibility(false)
    }

    private fun startErikSpelling(){
        val intent = Intent(this, SpellingActivity::class.java)
        startActivity(intent)
    }

    private fun startReading(){
        val intent = Intent(this, ReadingActivity::class.java)
        startActivity(intent)
    }

    private fun startIrregularVerbs(){
        val intent = Intent(this, IrregularVerbsActivity::class.java)
        startActivity(intent)
    }
/*
    private fun removeUserName(){
        val prefs = this.getSharedPreferences("", Context.MODE_PRIVATE)
        prefs.edit().remove(R.string.preferences_user_name.toString()).apply()
        prefs.edit().remove("SecretCode").apply()
    }
*/
    private fun setUserName(){
        userName = readSavedUser()
        if (userName == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readSavedUser() : String? {
        val prefs = this.getSharedPreferences("", Context.MODE_PRIVATE)
        return prefs.getString(R.string.preferences_user_name.toString(), null)
    }

    private fun addNewWords() {
        val intent = Intent(this, UploadSpellingWordsActivity::class.java)
        startActivity(intent)
    }

    private fun confirmRestoreSpellingFromLogs(){
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirm_restore_from_log))
            .setPositiveButton(getString(R.string.yes_button)) { _, _ -> restoreSpellingFromLogs() }
            .setNegativeButton(getString(R.string.no_button)) { dialogInterface, _ -> dialogInterface.cancel() }
            .setTitle(getString(R.string.restore_dialog_title))
            .create()
            .show()
    }


    private fun restoreSpellingFromLogs(){
        setPrBarVisibility(true)
        wordsProvider?.restoreSpellingWordsFromLogs { response -> onSpellingRestored(response) }
    }

    private fun onSpellingRestored(response: String){
        setPrBarVisibility(false)
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
    }

    private fun setPrBarVisibility(isVisible : Boolean){
        when(isVisible){
            true -> binding.mainProgressbar.root.visibility = View.VISIBLE
            false -> binding.mainProgressbar.root.visibility = View.GONE
        }
    }
}