package com.github.rezita.homelearning.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.FragmentAdminTabBinding
import com.github.rezita.homelearning.databinding.FragmentErikTabBinding
import com.github.rezita.homelearning.databinding.FragmentMarkTabBinding
import com.github.rezita.homelearning.network.WordsProvider

private const val ARG_NAME = "Name"
private const val ARG_POSITION = "Position"

class AdminTabFragment : Fragment() {
    private var paramName: String? = null
    private var paramPosition: Int? = null

    private var _binding: FragmentAdminTabBinding? = null
    private val binding get() = _binding!!

    private var wordsProvider: WordsProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramName = it.getString(ARG_NAME)
            paramPosition = it.getInt(ARG_POSITION)
        }
        wordsProvider = activity?.let { WordsProvider(it.applicationContext) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminTabBinding.inflate(inflater, container, false)
        binding.btnRestoreSpelling.setOnClickListener { confirmRestoreSpellingFromLogs() }
        setPrBarVisibility(false)
        return binding.root
    }

    private fun confirmRestoreSpellingFromLogs() {
        activity?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.confirm_restore_from_log))
                .setPositiveButton(getString(R.string.yes_button)) { _, _ -> restoreSpellingFromLogs() }
                .setNegativeButton(getString(R.string.no_button)) { dialogInterface, _ -> dialogInterface.cancel() }
                .setTitle(getString(R.string.restore_dialog_title))
                .create()
                .show()
        }
    }


    private fun restoreSpellingFromLogs() {
        setPrBarVisibility(true)
        wordsProvider?.restoreSpellingWordsFromLogs { response -> onSpellingRestored(response) }
    }

    private fun onSpellingRestored(response: String) {
        setPrBarVisibility(false)
        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
    }

    private fun setPrBarVisibility(isVisible: Boolean) {
        when (isVisible) {
            true -> binding.mainProgressbar.root.visibility = View.VISIBLE
            false -> binding.mainProgressbar.root.visibility = View.GONE
        }
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