package com.github.rezita.homelearning.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rezita.homelearning.databinding.FragmentErikTabBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.view.FillInSentencesActivity
import com.github.rezita.homelearning.view.SpellingActivity
import com.github.rezita.homelearning.view.UploadSpellingWordsActivity

private const val ARG_NAME = "Name"
private const val ARG_POSITION = "Position"

class ErikTabFragment : Fragment() {
    private var paramName: String? = null
    private var paramPosition: Int? = null

    private lateinit var binding: FragmentErikTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramName = it.getString(ARG_NAME)
            paramPosition = it.getInt(ARG_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentErikTabBinding.inflate(inflater, container, false)
        binding.btnStartSpelling.setOnClickListener { startErikSpelling() }
        binding.btnStartIrregular.setOnClickListener { startFillInSentences(SheetAction.READ_IRREGULAR_VERBS) }
        binding.btnStartHomophones.setOnClickListener { startFillInSentences(SheetAction.READ_HOMOPHONES) }
        binding.btnAddSpellingWords.setOnClickListener { addNewWords() }
        return binding.root
    }

    private fun startErikSpelling() {
        val intent = Intent(activity, SpellingActivity::class.java)
        intent.putExtra("action", SheetAction.READ_ERIK_SPELLING_WORDS.value)
        startActivity(intent)
    }

    private fun addNewWords() {
        val intent = Intent(activity, UploadSpellingWordsActivity::class.java)
        intent.putExtra("action", SheetAction.SAVE_ERIK_WORDS.value)
        startActivity(intent)
    }

    private fun startFillInSentences(sheetAction: SheetAction) {
        val intent = Intent(activity, FillInSentencesActivity::class.java)
        intent.putExtra("action", sheetAction.value)
        startActivity(intent)
    }


    companion object {
        @JvmStatic
        fun newInstance(paramName: String, paramPosition: Int) =
            ErikTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, paramName)
                    putInt(ARG_POSITION, paramPosition)
                }
            }
    }


}