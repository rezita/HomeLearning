package com.github.rezita.homelearning.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.FragmentErikTabBinding
import com.github.rezita.homelearning.databinding.FragmentMarkTabBinding
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.view.ReadingActivity
import com.github.rezita.homelearning.view.SpellingActivity
import com.github.rezita.homelearning.view.UploadSpellingWordsActivity

private const val ARG_NAME = "Name"
private const val ARG_POSITION = "Position"

class MarkTabFragment : Fragment() {
    private var paramName: String? = null
    private var paramPosition: Int? = null

    private var _binding: FragmentMarkTabBinding? = null
    private val binding get() = _binding!!


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
    ): View? {
        _binding = FragmentMarkTabBinding.inflate(inflater, container, false)
        binding.btnStartReading.setOnClickListener { startReading(SheetAction.READ_READING_WORDS) }
        binding.btnReadingCEW.setOnClickListener { startReading(SheetAction.READ_READING_CEW) }
        binding.btnStartMarkSpelling.setOnClickListener{startMarkSpelling()}
        binding.btnAddMarkSpellingWords.setOnClickListener { addNewWords() }
        return binding.root

    }

    private fun startReading(readingAction: SheetAction) {
        val intent = Intent(activity, ReadingActivity::class.java)
        intent.putExtra("action", readingAction.value)
        startActivity(intent)
    }

    private fun startMarkSpelling() {
        val intent = Intent(activity, SpellingActivity::class.java)
        intent.putExtra("action", SheetAction.READ_MARK_SPELLING_WORDS.value)
        startActivity(intent)
    }

    private fun addNewWords() {
        val intent = Intent(activity, UploadSpellingWordsActivity::class.java)
        intent.putExtra("action", SheetAction.SAVE_MARK_WORDS.value)
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